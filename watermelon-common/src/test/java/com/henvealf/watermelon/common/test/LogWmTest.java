package com.henvealf.watermelon.common.test;

import com.henvealf.watermelon.common.LogWmUtil;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author hongliang.yin/Henvealf on 2018/10/27
 */
public class LogWmTest {

    Logger log = LogWmUtil.getLog(LogWmTest.class);

    @Test
    public void testDefaultLog() {
        log.info("before set properties");
        LogWmUtil.setLog4jProperties("com/henvealf/watermelon/common/log4j-to-err.properties");
        log.info("after set properties");
    }
}
