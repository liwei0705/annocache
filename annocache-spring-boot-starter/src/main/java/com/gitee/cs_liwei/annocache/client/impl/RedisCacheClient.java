package com.gitee.cs_liwei.annocache.client.impl;

import com.gitee.cs_liwei.annocache.client.iface.ICacheClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

/**
 * cache client implementation 1：based on Redis
 */
@Slf4j
@Repository
public class RedisCacheClient implements ICacheClient {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value, Duration ofSeconds) {
        redisTemplate.opsForValue().set(key,value, ofSeconds);
    }
}
