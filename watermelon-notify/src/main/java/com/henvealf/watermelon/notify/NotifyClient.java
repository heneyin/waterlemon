package com.henvealf.watermelon.notify;

import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.io.Closeable;
import java.io.IOException;

/**
 * 通知客户端，负责发起通知与接收通知（使用监听器）。
 * @author hongliang.yin/Henvealf on 2018/12/17
 */
public class NotifyClient implements Closeable {

    private CuratorFramework cf = null;
    private NodeCache nodeCache = null;
    private String notifiedPath = null;

    public NotifyClient(String zkAddress, String path)  {
        Preconditions.checkNotNull(path, "the zk path that notify used can't be null");
        this.cf = CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000,3));
        this.notifiedPath = path;
    }


    public void setNotifyListener(final NotifyListener notifyListener) {
        // 设置了监听器才能使用到 nodeCache
        this.nodeCache = new NodeCache(cf, notifiedPath);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                notifyListener.process();
            }
        });
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
        try {
            this.cf.setData().forPath(this.notifiedPath, "123".getBytes());
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


    public static NotifyClient getClient()  {
        return null;
    }

}

