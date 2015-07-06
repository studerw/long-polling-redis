package com.studerw.redis;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Set;

/**
 * User: Bill Studer
 * Date: 7/5/2015
 */
public class FullRedisTest {
    private static final Logger log = LoggerFactory.getLogger(FullRedisTest.class);

    @Test
    public void testRedisServer() throws IOException {
        RedisServer redisServer = new RedisServer();
        redisServer.start();
        Assert.assertTrue(redisServer.isActive());
        JedisConnectionFactory fact = new JedisConnectionFactory();
        org.springframework.data.redis.core.RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(fact);
        Set keys = template.keys("*");
        log.debug("Number of keys: {}", keys.size());
        redisServer.stop();

    }
}
