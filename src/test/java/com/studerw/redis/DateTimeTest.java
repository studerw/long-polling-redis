package com.studerw.redis;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author William Studer
 */
public class DateTimeTest {

    @Test
    public void dateTest(){
        DateTime dt = new DateTime();
        System.out.println(dt.toString());
    }
}
