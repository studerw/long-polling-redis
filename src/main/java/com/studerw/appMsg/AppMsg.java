package com.studerw.appMsg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by studerw on 7/10/2015.
 */
public class AppMsg implements Serializable, Comparator<AppMsg> {

    private Integer id;
    private String message;
    private Long timeStamp;

    public AppMsg(Integer id, String message) {
        this.id = id;
        this.message = message;
        this.timeStamp = System.currentTimeMillis();
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

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
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

        AppMsg appMsg = (AppMsg) o;

        return id.equals(appMsg.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compare(AppMsg o1, AppMsg o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
