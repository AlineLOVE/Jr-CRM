package com.corp.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Created by Administrator on 2023/2/7.
 */

public class TestRedis {
    /**
     *   自动装配RedisTemplate对象
     */
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     *  首先我们知道Redis是一款key，value存储结构的数据库
     *
     *   因此我们假定这个set()测试方法以key，value的形式往Redis数据库中存储数据
     *
     *   key，value形式的存储命令：set
     *   key，value形式的取数据命令：get
     *
     */

    /*@Test*/
    void set() {
        ValueOperations ops = redisTemplate.opsForValue();    // 首先redisTemplate.opsForValue的目的就是表明是以key，value形式储存到Redis数据库中数据的
        ops.set("address1","zhengzhou");// 到这里就表明Redis数据库中存储了key为address1，value为zhengzhou的数据了（取的时候通过key取数据）
    }

    /**
     *  取数据
     */
    void get() {
        ValueOperations ops = redisTemplate.opsForValue();  // 表明取的是key，value型的数据
        Object o = ops.get("address1");  // 获取Redis数据库中key为address1对应的value数据
        System.out.println(o);
    }
}

