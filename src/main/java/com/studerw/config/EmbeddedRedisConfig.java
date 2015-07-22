package com.studerw.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import redis.embedded.RedisServer;

import java.io.IOException;

/**
 * @author studerw
 */
@Configuration
@Profile("embedded")
public class EmbeddedRedisConfig {
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedRedisConfig.class);

    @Autowired
    private Environment env;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        String host = env.getProperty("app.redis.host", String.class);
        Integer port = env.getProperty("app.redis.port", Integer.class);
        LOG.info("Creating new embedded Redis Server at {}:{}", host, port);
        return new RedisServer(port);
    }
}
