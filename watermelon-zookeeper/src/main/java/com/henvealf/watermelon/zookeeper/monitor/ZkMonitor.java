package com.henvealf.watermelon.zookeeper.monitor;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.rmi.server.ExportException;

/**
 * zk 监控器
 */
public class ZkMonitor implements Watcher {

    private ZooKeeper zk;
    private String znode;
    private ZkNodeExistsListener nodeExistsListener;
    private ZkNodeWriteListener zkNodeWriteListener;


    public ZkMonitor(String zkServer, String znode, int sessionTimeout){

        try {
            this.zk = new ZooKeeper(zkServer, sessionTimeout, this);
        } catch (IOException e) {
            throw new RuntimeException("create zk instance failed.", e);
        }
        this.znode = znode;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {

    }


}


