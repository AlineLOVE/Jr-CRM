package com.corp.jr.controller;

import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.apache.logging.log4j.core.util.datetime.FixedDateFormat.FixedTimeZoneFormat.HH;

/**
 * Created by Administrator on 2023/2/13.
 */
@RestController
@RequestMapping("/readWriteLock")
public class ReadWriteLock {
    private Logger log = LoggerFactory.getLogger(ReadWriteLock.class);
    @Autowired
    RedissonClient redisson;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 读写锁（ReadWriteLock） 测试1
     * 读锁：共享锁，所有线程共享读锁
     * 写锁: 独占锁,只能单独线程占用，执行完才能执行其他“读写锁”。
     */
    @ResponseBody
    @GetMapping("/test/readWriteLock/toRead")
    public String toReadLock() {
        //获取 锁
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("readWriteLock");
        RLock rLock = readWriteLock.readLock();
        //上 读锁
        rLock.lock();
        String writeValue = "";
        try {
            log.info("【读锁-{}】加锁成功，读数据...", Thread.currentThread().getId());
           Object object = redisTemplate.opsForValue().get("writeValue");
            writeValue =  redisTemplate.opsForValue().get("writeValue").toString();
        } finally {
            log.info("【读锁-{}】解锁成功,uuid={}", Thread.currentThread().getId(),writeValue);
            //解锁
            rLock.unlock();
        }
        return writeValue;
    }

    /**
     * 读写锁（ReadWriteLock） 测试2
     */
    @ResponseBody
    @GetMapping("/test/readWriteLock/toWrite")
    public String toWriteLock() {
        //获取 锁
        RReadWriteLock readWriteLock = redisson.getReadWriteLock("readWriteLock");
        RLock rLock = readWriteLock.writeLock();
        //上 写锁
        rLock.lock();
        String uuid = UUID.randomUUID()
                .toString();
        try {
            log.info("【写锁-{}】加锁成功，睡眠3秒，模拟执行业务...", Thread.currentThread()
                    .getId());
            Thread.sleep(3000);
            redisTemplate.opsForValue()
                    .set("writeValue", uuid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("【写锁-{}】解锁成功，uuid={}", Thread.currentThread()
                    .getId(),uuid);
            //解锁
            rLock.unlock();
        }
        return "writeLock ok,uuid = " + uuid;
    }
    /**
     * 信号量
     *  RSemaphore基于 Redis 的Semaphore开发。
     *  使用RSemaphore获取资源的顺序是不可预测的，所以它是一种非公平锁。
     *  可以理解为分布式的信号量，它的作用是用来限制同时访问共享区域的线程数量。
     */
    @GetMapping(value = "/permit/park")
    @ResponseBody
    public String park() {
        RSemaphore park = redisson.getSemaphore("park");
        try {
            log.info("park可用许可证的数量:"+park.availablePermits());
            log.info("park尝试获取当前可用的许可证:"+park.tryAcquire());
            park.acquire();// 获取一个信号量（redis中信号量值-1）,如果redis中信号量为0了，则在这里阻塞住，直到信号量大于0，可以拿到信号量，才会继续执行。
            log.info("park获取到许可证，continue......");
            log.info("park可用许可证的数量:"+park.availablePermits());//获取后-1
            log.info("park获取当前可用的许可证:"+park.tryAcquire());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "ok";
    }
    @GetMapping(value = "/permit/go")
    @ResponseBody
    public String go(int p) {
        RSemaphore park = redisson.getSemaphore("park");
        log.info("go可用许可证的数量1:"+park.availablePermits());
        log.info("go尝试获取当前可用的许可证:"+park.tryAcquire());
        park.trySetPermits(p);//尝试设置许可证数量。
//        if(p>0){
//            park.release(p);////释放信号量
//        }else {
//            park.release();  //释放一个信号量（redis中信号量值+1）
//        }
        log.info("go可用许可证的数量2:"+park.availablePermits());
        log.info("go尝试获取当前可用的许可证:"+park.tryAcquire());
        return "ok";
    }
    /**
     * 闭锁
     * 在要完成某些运算时，只有其它线程的运算全部运行完毕，当前运算才继续下去。
     * 模拟场景： 学校放假，学校门卫锁门必须等待所有班级全部离开，才将学校大门锁住。
     * @return
     */
    @ResponseBody
    @GetMapping(value = "lockDoor")
    public String lockDoor(){
        RCountDownLatch lockDoor = redisson.getCountDownLatch("lockDoor");
        Date day=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lockDoor.trySetCount(5);//设置计数
        log.info("lockDoor计数："+lockDoor.getCount());
        try {
            log.info("进入等待。。。。。"+ sdf.format(day));
            lockDoor.await();//等待闭锁完成......boolean await(long var1, TimeUnit var3) throws InterruptedException;
            Date day2=new Date();
            log.info("等待结束，锁门...."+sdf.format(day2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "锁门";
    }
    @ResponseBody
    @GetMapping(value = "/go/{id}")
    public String go(@PathVariable("id") Integer id) throws InterruptedException {
        RCountDownLatch lockDoor = redisson.getCountDownLatch("lockDoor");
        log.info("lodkDoor当前计数:"+lockDoor.getCount());
        lockDoor.countDown();//计数减1
        Thread.sleep(2000);//等待两秒
        log.info("lodkDoor当前计数:"+lockDoor.getCount());
        return id+"完成";
    }
    /**
     * 分布式锁
     */
    @ResponseBody
    @GetMapping("/sentx/lock")
    public String sentxLock() {
        //通过java代码实现SETNX同时设置过期时间
//key--键   value--值  time--过期时间  TimeUnit--时间单位枚举
        //stringRedisTemplate.opsForValue().setIfAbsent(key, value , time, TimeUnit);

//        redisTemplate.opsForValue().setIfAbsent()

        return "";
    }

}
