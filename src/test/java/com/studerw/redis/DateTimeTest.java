package com.studerw.redis;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by studerw on 7/10/2015.
 */
public class DateTimeTest {

    @Test
    public void dateTest(){
        DateTime dt = new DateTime();
        System.out.println(dt.toString());

    }
}
