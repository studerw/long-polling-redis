package com.studerw.appMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author studerw
 */
@Component("appMsgHandler")
public class AppMsgHandler implements MessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(AppMsgHandler.class);
    private final static Charset UTF8 = StandardCharsets.UTF_8;

    @Autowired ConcurrentHashMap<DeferredResult<List<AppMsg>>, Integer> waitingRequests;
    @Autowired AppMsgRepo appMsgRepo;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        LOG.info("RedisPub: {} on Channel: {}", new String(message.getBody(), UTF8), new String(message.getChannel(), UTF8));
        Iterator<Map.Entry<DeferredResult<List<AppMsg>>, Integer>> it = waitingRequests.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<DeferredResult<List<AppMsg>>, Integer> entry = it.next();
            List<AppMsg> messages = appMsgRepo.readSubset(entry.getValue());
            entry.getKey().setResult(messages);
            it.remove();
        }
    }
}
