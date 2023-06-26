package com.zic.springboot.demo.zicCoolApp.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//The Primary tell spring to choose this implementation if there are more out there.
//The default scope of bean is singleton, so this is just a demo.
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Primary
@Component
public class RedisPingTask {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisPingTask(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 60000) // Run every 1 minute
    public void pingRedis() {
        redisTemplate.getConnectionFactory().getConnection().ping();
        //ToDo:Change this to logging
        System.out.println("Ping sent to Redis");
    }
}
