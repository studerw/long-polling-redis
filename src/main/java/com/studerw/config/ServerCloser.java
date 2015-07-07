package com.studerw.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;

/**
 * @author studerw
 */

@Component
public class ServerCloser {
    private static final Logger LOG = LoggerFactory.getLogger(ServerCloser.class);

    @Autowired RedisServer redisServer;

    @PreDestroy
    public void preDestroy() {
        LOG.info("Shutting down Redis Server");
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();

        }
    }
}
