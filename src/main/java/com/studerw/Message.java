package com.studerw;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by studerw on 7/10/2015.
 */
public class Message implements Serializable, Comparator<Message> {

    private Integer id;
    private String message;
    private DateTime timeStamp;

    public Message(Integer id, String message) {
        this.id = id;
        this.message = message;
        this.timeStamp = new DateTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("message", message)
                .append("timeStamp", timeStamp)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return id.equals(message.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compare(Message o1, Message o2) {
        return o1.getTimeStamp().compareTo(o2.getTimeStamp());
    }
}
