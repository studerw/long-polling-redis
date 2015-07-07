package com.studerw.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * User: William Studer
 * Date: 7/5/2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:test-redis-context.xml"})
public class RedisTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedisTest.class);
    private static final String PREFIX = RedisTest.class.getSimpleName();
    private static final String UTF8 = StandardCharsets.UTF_8.displayName();

    @Autowired RedisServer redisServer;
    @Autowired RedisTemplate redisTemplate;
    @Autowired RedisConnectionFactory connectionFactory;

    @Test
    public void testRedisServer() throws IOException {
        try {
            Assert.assertNotNull(redisServer);
            boolean isActive = redisServer.isActive();
            LOG.debug("IsActive: {}", isActive);
            Assert.assertTrue("Redis server is started", isActive);
        }
        finally {
            if (redisServer != null && redisServer.isActive()) {
                redisServer.stop();
            }
        }
    }

    @Test
    public void testRedisWrite() throws IOException {

        try {
            RedisConnection conn = connectionFactory.getConnection();
            for (int i = 0; i < 100; ++i) {
                conn.set((PREFIX + '-' + i).getBytes(UTF8), UUID.randomUUID().toString().getBytes(UTF8));
            }

            byte[] keyPattern = (PREFIX + "*").getBytes(UTF8);
            Set<byte[]> keys = conn.keys(keyPattern);
            for (byte[] key : keys) {
                String decodedKey = new String(key, UTF8);
                String decodedVal = new String(conn.get(key), UTF8);
                LOG.debug("{} ---> {}", decodedKey, decodedVal);
            }
            Assert.assertTrue(keys.size() == 100);
        }
        finally {
            if (redisServer != null && redisServer.isActive()) {
                redisServer.stop();
            }
        }
    }
}
