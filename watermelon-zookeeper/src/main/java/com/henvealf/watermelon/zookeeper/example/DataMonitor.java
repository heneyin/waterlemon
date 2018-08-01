package com.henvealf.watermelon.zookeeper.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Arrays;

/**
 *
 * @author hongliang.yin/Henvealf on 2018/7/26
 */
public class DataMonitor implements Watcher, AsyncCallback.StatCallback {

    String znode;
    ZooKeeper zk;
    boolean dead;
    Watcher chainedWatcher;
    DataMonitorListener dataMonitorListener;

    byte[] prevData;

    public DataMonitor(ZooKeeper zookeeper, String znode, Watcher chainedWatcher,
                       DataMonitorListener listener) {
        this.zk = zookeeper;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.dataMonitorListener = listener;
        zk.exists(znode, true, this, null);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        // 监控连接状态的改变。
        String path = watchedEvent.getPath();
        if (watchedEvent.getType() == Event.EventType.None) {
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    dead = true;
                    dataMonitorListener.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
            }
        } else {
            if (path != null && path.equals(znode)) {
                zk.exists(znode, true, this, null);
            }
        }
        if (chainedWatcher != null) {
            chainedWatcher.process(watchedEvent);
        }
    }


    @Override
    public void processResult(int rc, String s, Object o, Stat stat) {
        boolean exists;
        switch (rc) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists =false;
                break;
            case KeeperException.Code.SessionExpired:
            case KeeperException.Code.NoAuth:
                dead = true;
                dataMonitorListener.closing(rc);
                return;
            default:
                zk.exists(znode,true, this, null);
                return;
        }

        byte[] b = null;
        if (exists) {
            try {
                b = zk.getData(znode, false, null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
                return;
            }
        }

        if (b == null && b != prevData
                || (b != null && !Arrays.equals(prevData, b))) {
            dataMonitorListener.exists(b);
            prevData = b;
        }
    }


    public interface DataMonitorListener {

        /**
         * 节点“存在”的状态发生了状态
         */
        void exists(byte[] data);

        /**
         * zookeeper seesion 的状态不再可用。
         * @param rc zookeeper 结果码
         */
        void closing(int rc);
    }
}
