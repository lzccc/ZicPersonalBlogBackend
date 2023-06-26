package com.zic.springboot.demo.zicCoolApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//The Lazy is just tell Spring that don't initialize bean if it is not required.
@Service
@Lazy
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithTTL(String key, Object value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void storeHash(String key, Map<String, Object> hash) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, hash);
    }

    public Map<String, Object> getHash(String key) {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    public void storeListOfHashes(String key, List<String> hashKeys) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        for (String hashKey : hashKeys) {
            if (hasKey(hashKey)) {
                listOperations.leftPush(key, hashKey);
            }
        }
    }

    public List<Object> getList(String key) {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        return listOperations.range(key, 0, -1);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
