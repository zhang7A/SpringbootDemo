package com.example.redis;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
class RedisApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void connectRedis() {
        redisTemplate.opsForValue().set("name", "zhang");
        String name = (String) redisTemplate.opsForValue().get("name");
        Assert.assertEquals("zhang", name);
    }


}
