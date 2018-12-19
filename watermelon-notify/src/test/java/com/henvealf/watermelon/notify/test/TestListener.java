package com.henvealf.watermelon.notify.test;

import com.henvealf.watermelon.notify.NotifyClient;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author hongliang.yin/Henvealf on 2018/12/17
 */
public class TestListener {

    @Test
    public void pre() throws Exception {
        NotifyClient notifyClient = new NotifyClient("localhost:2181", "/henvealf/notify");
        notifyClient.setNotifyListener(() -> {
            System.out.println("I have be notified");
        });
        notifyClient.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.read();
        notifyClient.close();
    }

}
