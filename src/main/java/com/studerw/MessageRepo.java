package com.studerw;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by studerw on 7/10/2015.
 */
@Component("messageRepo")
public class MessageRepo {

    public static AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private List<Message> messages = new CopyOnWriteArrayList<>();



    public Message create(String msg) {
        Integer id = ID_GENERATOR.getAndIncrement();
        Message message = new Message(id, msg);
        messages.add(message);
        return message;
    }

    public List<Message> readAll() {
        List<Message> temp = this.messages;
        return Collections.unmodifiableList(temp);
    }

    public List<Message> readSubset(Integer startId) {
        int size = this.messages.size();
        List<Message> temp = this.messages.subList(startId, size);
        return Collections.unmodifiableList(temp);
    }


}
