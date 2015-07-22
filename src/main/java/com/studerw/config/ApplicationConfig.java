package com.studerw.config;

import com.studerw.appMsg.AppMsgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import redis.embedded.RedisServer;

import java.io.IOException;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(basePackages = {"com.studerw"}, excludeFilters = @Filter({Controller.class, Configuration.class, ControllerAdvice.class}))
@PropertySource("classpath:config/app.properties")
@Import(value = EmbeddedRedisConfig.class)
class ApplicationConfig {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    @Autowired
    private Environment env;
    @Autowired
    private AppMsgHandler appMsgHandler;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(appMsgHandler);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer mlc = new RedisMessageListenerContainer();
        mlc.setConnectionFactory(redisConnectionFactory());
        String topicName = env.getProperty("app.topic.name");
        LOG.info("Adding MessageListenerAdapter[appMsgHandler] to topic: {}", topicName);
        mlc.addMessageListener(messageListener(), new PatternTopic(topicName));
        return mlc;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory cf = new JedisConnectionFactory();
        String host = this.env.getProperty("app.redis.host");
        Integer port = this.env.getProperty("app.redis.port", Integer.class);
        LOG.info("Creating new Redis Connection factory at host={}, port={}.", host, port);
        cf.setHostName(host);
        cf.setPort(port);
        cf.setUsePool(true);
        return cf;
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}
