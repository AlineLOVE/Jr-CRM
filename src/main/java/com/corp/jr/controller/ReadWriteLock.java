package com.corp.jr.controller;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

}
