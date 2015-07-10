package com.studerw.config;

import com.studerw.appMsg.AppMsgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.async.DeferredResult;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(basePackages = {"com.studerw"}, excludeFilters = @Filter({Controller.class, Configuration.class}))
@PropertySource("classpath:config/app.properties")
class ApplicationConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    @Autowired private Environment env;
    @Autowired private AppMsgHandler appMsgHandler;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        String host = env.getProperty("app.redis.host", String.class);
        Integer port = env.getProperty("app.redis.port", Integer.class);
        LOG.debug("Creating new embedded Redit Server at {}:{}", host, port);
        return new RedisServer(port);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer mlc = new RedisMessageListenerContainer();
        mlc.setConnectionFactory(redisConnectionFactory());
        String topicName = env.getProperty("app.topic.name");
        LOG.info("Adding MessageHandler to topic: {}", topicName);
        mlc.addMessageListener(appMsgHandler, new PatternTopic(topicName));
        return mlc;
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

    @Bean(name = "waitingRequests")
    public ConcurrentHashMap<DeferredResult, String> waitingRequests() {
        return new ConcurrentHashMap<>();
    }

}
