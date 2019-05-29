package com.studerw.redis;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author William Studer
 */
public class DateTimeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    public void dateTest(){
        DateTime dt = new DateTime();
        LOGGER.debug(dt.toString());
    }
}
