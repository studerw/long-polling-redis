package com.studerw.appMsg;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of {@link AppMsgRepo} using in-memory Concurrent HashMap
 * @author William Studer
 */
@Component
public class MemoryAppMsgRepo implements AppMsgRepo {
    private static final Logger LOG = LoggerFactory.getLogger(AppMsgRepo.class);
    private  AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    protected List<AppMsg> appMsgs = new CopyOnWriteArrayList<>();

    @Autowired protected StringRedisTemplate redisTemplate;

    @Value("${app.topic.name}")
    protected String topicName;


    @Override
    public int count() {
        return this.appMsgs.size();
    }

    @Override
    public int deleteAll() {
        int count = appMsgs.size();
        LOG.info("Deleting all: {} messages", count);
        this.appMsgs.clear();
        LOG.info("Publishing change to Redis: {}", -1);
        this.redisTemplate.convertAndSend(topicName, "-1");
        return count;
    }

    @Override
    public AppMsg create(String msg) {
        Integer id = ID_GENERATOR.getAndIncrement();
        AppMsg appMsg = new AppMsg(id, msg);
        LOG.info("Added new message: {}", appMsg);
        appMsgs.add(appMsg);
        LOG.info("Publishing change to Redis: {}", id);
        this.redisTemplate.convertAndSend(topicName, String.valueOf(id));
        return appMsg;
    }

    @Override
    public List<AppMsg> readAll() {
        LOG.debug("Reading all messages...");
        List<AppMsg> temp = Lists.newArrayList(this.appMsgs);
        Collections.sort(temp);
        return temp;
    }

    @Override
    public List<AppMsg> readSubset(Integer startId) {
        int size = this.appMsgs.size();
        LOG.info("Reading messages from {} - {}", startId, size);
        List<AppMsg> temp = Lists.newArrayList(this.appMsgs.subList(startId, size));
        Collections.sort(temp);
        return temp;
    }
}
