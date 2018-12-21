package com.henvealf.watermelon.notify.test;

import com.henvealf.watermelon.notify.NotifyClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hongliang.yin/Henvealf on 2018/12/17
 */
public class TestListener {

    @Test
    public void pre() throws Exception {

        AtomicInteger count = new AtomicInteger();

        NotifyClient notifyClient = new NotifyClient("localhost:2181", "/henvealf/notify");

        notifyClient.setNotifyListener((currentData) -> {
            System.out.println("I have be notified");
            count.getAndIncrement();
            System.out.println("total count:" + count.get() + ", current data:" + new String(currentData.getData()));
        });

        notifyClient.start();


        Thread.sleep(1000 * 20);

        notifyClient.close();


    }

    /**
     * 测试监视已经存在的路径的 watch 初次创建的时候，会不会收到通知。
     */
    @Test
    public void testWatch() throws Exception {

        // 不会触发
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("watchEvent:" + watchedEvent.getType().name());
            }
        };

        // 一定会触发，且触发类型依赖于具体操作。
        BackgroundCallback backgroundCallback = new BackgroundCallback()
        {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception
            {
                System.out.println("backgroundCallback event: " + event.getType().name());
            }
        };

        CuratorFramework cf = CuratorFrameworkFactory.newClient("localhost:2181", new RetryOneTime(1000));
        cf.start();

        cf.checkExists().usingWatcher(watcher).inBackground(backgroundCallback).forPath("/henvealf");
//        Thread.sleep(1000 * 4);
//        cf.setData().forPath("/henvealf", "123".getBytes());
        Thread.sleep(1000 * 4);
        cf.close();
    }

    @Test
    public void testAddress() throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println(hostAddress);

        hostAddress = InetAddress.getLocalHost().getHostName();
        System.out.println(hostAddress);

        hostAddress = InetAddress.getLocalHost().getCanonicalHostName();
        System.out.println(hostAddress);
    }

}
