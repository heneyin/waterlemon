package com.henvealf.watermelon.notify;

import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.omg.CORBA.NO_IMPLEMENT;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;

/**
 * 通知客户端，实现了通知发起与通知接受（使用监听器）的功能。
 * 使用 Curator 的 NodeCache 实现。
 *
 * 发起通知：<code>sendNotify()</code>
 *
 * 设置通知监听器： <code>setNotifyListener(NotifyListener notifyListener)</code>
 *
 * 启动： <code>start()</code> 且启动时会默认触发一次通知。
 * 停止： <code>close()</code>
 *
 * 因为 ZK 监听的的特性，在高并发的发起一批通知时，接受端不能保证会接受到每一个通知，但一定能收到最后一个发起的通知。
 * 所以该工具时适合于简单的通知某件事情需要去做的时候去使用。
 * 当需要通知之间具有互相依赖，类似于流程步骤的关系时，请勿使用该工具。
 *
 * @author hongliang.yin/Henvealf on 2018/12/17
 */
public class NotifyClient implements Closeable {

    private CuratorFramework cf = null;
    private NodeCache nodeCache = null;
    private String notifiedPath = null;

    private static final String DEFAULT_ID = "default-id";

    public NotifyClient(String zkAddress, String basePath)  {
        Preconditions.checkNotNull(basePath, "The zk path that notify used can't be null");
        this.cf = CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000,3));
        this.notifiedPath = basePath;
    }

    /**
     * 当你想要接受到通知，就需要添加一个接收通知的监听器。
     * @param notifyListener
     */
    public void setNotifyListener(String id, final NotifyListener notifyListener) {
        // 设置了监听器才能使用到 nodeCache
        this.nodeCache = new NodeCache(cf, notifiedPath + "/" + id);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData currentData = nodeCache.getCurrentData();
                notifyListener.process(currentData);
            }
        });
    }

    public void setNotifyListener(final NotifyListener notifyListener) {
        setNotifyListener(DEFAULT_ID, notifyListener);
    }



    public void start() throws Exception {
        cf.start();
        if ( null == cf.checkExists().forPath(this.notifiedPath) ) {
            cf.create().creatingParentsIfNeeded().forPath(this.notifiedPath);
        }
        if ( null != nodeCache) {
            nodeCache.start();
        }
    }

    public void sendNotify() {
        sendNotify(DEFAULT_ID);
    }

    /**
     * send a notify
     * @param id the notify id, like a type, not the notify content.
     */
    public void sendNotify(String id) {
        try {

            String nowPath = this.notifiedPath + "/" + id;
            if ( null == cf.checkExists().forPath(nowPath) ) {
                cf.create().creatingParentsIfNeeded().forPath(nowPath);
            }
            this.cf.setData().forPath(nowPath,
                    (InetAddress.getLocalHost().getHostAddress() + "/" + InetAddress.getLocalHost().getHostName()).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        if (null != cf) {
            this.cf.close();
        }
        if (null != nodeCache) {
            try {
                this.nodeCache.close();
            } catch (IOException e) {
                throw new RuntimeException("Error happened when close NotifyClient " + this, e);
            }
        }
    }

}

