package com.corp.jr.controller;

import com.corp.jr.model.generator.CoreUser;
import com.corp.jr.util.redis.RedisUtils;
import com.corp.jr.web.JsonResult;
import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/2/7.
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    private Logger log = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringredisTemplate;
    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/getvalue")
    public Object getValue(String key) {
        log.info("key值，key:"+key);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object object =valueOperations.get(key);
        log.info("value值，value:"+object);
        return object;
    }

    @PostMapping("/setvalue")
    public Object setValue(@RequestBody Map map) {
        log.info("key值，key:"+map.get("key")+",value:"+map.get("value"));
        if( map.get("key")==null || map.get("value")==null){
            return JsonResult.failMessage("请检查key或value关键字");
        }
        String key = String.valueOf(map.get("key"));
        String value =String.valueOf(map.get("value"));
        if(map.get("expireTime")!=null && map.get("expireTime").toString().matches("[0-9]+")){
            long expireTime = Long.parseLong( map.get("expireTime").toString());
            redisUtils.set(key,value,expireTime);
            return JsonResult.successMessage("Success");
        }
        redisUtils.set(key,value);
        return JsonResult.successMessage("Success");
    }


}
