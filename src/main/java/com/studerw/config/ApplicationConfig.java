package com.studerw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.async.DeferredResult;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(excludeFilters = @Filter({Controller.class, Configuration.class}))
@PropertySource("classpath:config/app.properties")
class ApplicationConfig {

    @Autowired Environment env;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        return new RedisServer(env.getProperty("app.redis.port", Integer.class));
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory cf = new JedisConnectionFactory();
        String host = this.env.getProperty("app.redis.host");
        Integer port = this.env.getProperty("app.redis.port", Integer.class);
        cf.setHostName(host);
        cf.setPort(port);
        cf.setUsePool(true);
        return cf;
    }


    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean(name = "waitingRequest")
    public ConcurrentHashMap<DeferredResult, String> waitingRequests() {
        return new ConcurrentHashMap<>();
    }

}
