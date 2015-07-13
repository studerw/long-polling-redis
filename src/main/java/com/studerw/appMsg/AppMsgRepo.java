package com.studerw.appMsg;

import java.util.List;

/**
 * @author William Studer
 */
public interface AppMsgRepo {


    /**
     * @return count of all messages
     */
    int count();

    /**
     * Delete all messages
     * @return count of deleted messages
     */
    int deleteAll();

    /**
     * Create a new message. The id and timeStamp will be set by the backend
     * @param msg
     * @return the newly created {@link AppMsg AppMsg} with all attributes set.
     */
    AppMsg create(String msg);

    /**
     * @return a sorted list of all @{link AppMsg AppMsgs}
     */
    List<AppMsg> readAll();

    /**
     *
     * @param startId the start index of the messages to read
     * @return subset of all messages, beginning with {@code startId} param inclusive
     */
    List<AppMsg> readSubset(Integer startId);
}
