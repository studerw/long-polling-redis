package com.studerw.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * User: William Studer
 * Date: 7/5/2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:test-redis-context.xml"})
public class RedisTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedisTest.class);

    @Autowired RedisServer redisServer;
    @Autowired RedisTemplate redisTemplate;

    @Test
    public void testRedisServer() throws IOException {
        try {
            Assert.assertNotNull(redisServer);
            boolean isActive = redisServer.isActive();
            LOG.debug("IsActive: {}", isActive);
            Assert.assertTrue("Redis server is started", isActive);
        } finally {
            if (redisServer != null && redisServer.isActive()){
                redisServer.stop();
            }
        }
    }

    @Test
    public void testRedisWrite() throws IOException {
        try {

        } finally {
            if (redisServer != null && redisServer.isActive()){
                redisServer.stop();
            }
        }
    }
}
