package com.studerw.appMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author studerw
 */
@Component("appMsgHandler")
public class AppMsgHandler implements MessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(AppMsgHandler.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LOG.warn("GOT MESSAGE: {}", message);
        LOG.warn("pattern: {}", new String(pattern, StandardCharsets.UTF_8));
    }
}
