package com.studerw.appMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by studerw on 7/10/2015.
 */
@Component("messageRepo")
public class AppMsgRepo {
    private static final Logger LOG = LoggerFactory.getLogger(AppMsgRepo.class);
    public static AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    protected List<AppMsg> appMsgs = new CopyOnWriteArrayList<>();

    @Autowired protected RedisTemplate redisTemplate;

    @Value("${app.topic.name}")
    protected String topicName;


    public int count() {
        return this.appMsgs.size();
    }

    public int deleteAll() {
        int count = appMsgs.size();
        LOG.info("Deleting all: {} messages", count);
        this.appMsgs.clear();
        LOG.info("Publishing change to Redis: {}", -1);
        this.redisTemplate.convertAndSend(topicName, -1);
        return count;
    }

    public AppMsg create(String msg) {
        Integer id = ID_GENERATOR.getAndIncrement();
        AppMsg appMsg = new AppMsg(id, msg);
        LOG.info("Added new message: {}", appMsg);
        appMsgs.add(appMsg);
        LOG.info("Publishing change to Redis: {}", id);
        this.redisTemplate.convertAndSend(topicName, id);
        return appMsg;
    }

    public List<AppMsg> readAll() {
        LOG.debug("Reading all messages...");
        List<AppMsg> temp = this.appMsgs;
        return Collections.unmodifiableList(temp);
    }

    public List<AppMsg> readSubset(Integer startId) {
        int size = this.appMsgs.size();
        LOG.debug("Reading messages from {} - {}", startId, size);
        List<AppMsg> temp = this.appMsgs.subList(startId, size);
        return Collections.unmodifiableList(temp);
    }

}
