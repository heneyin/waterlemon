package com.henvealf.watermelon.notify.test;

import com.henvealf.watermelon.notify.NotifyClient;
import org.junit.Test;

/**
 * @author hongliang.yin/Henvealf on 2018/12/17
 */
public class TestNotify {

    @Test
    public void pre() throws Exception {
        NotifyClient notifyClient = new NotifyClient("localhost:2181", "/henvealf/notify");
        notifyClient.start();

        for (int i = 0; i < 10; i++) {
            notifyClient.sendNotify();
//            Thread.sleep(100);
        }

        Thread.sleep(1000);
        notifyClient.close();
    }



}
