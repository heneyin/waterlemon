package com.henvealf.watermelon.zookeeper.example;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;

/**
 *
 * @author hongliang.yin/Henvealf on 2018/7/26
 */
public class Executor implements Watcher, Runnable, DataMonitor.DataMonitorListener {
    String znode;
    DataMonitor dm;
    ZooKeeper zk;
    String fileName;
    String exec[];
    Process child;

    public Executor(String hostPort, String znode, String fileName, String exec[]) throws IOException {
        this.fileName = fileName;
        this.exec = exec;
        this.znode = znode;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(zk, znode, null, this);
    }

    @Override
    public void exists(byte[] data) {
        // 为空说明为删除操作
        if (data == null) {
            if (child != null) {
                System.out.println("killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("stop child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("Starting child");
                child = Runtime.getRuntime().exec(exec);
                new StreamWriter(child.getInputStream(), System.out);
                new StreamWriter(child.getErrorStream(), System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        dm.process(event);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                // 线程等待
                // 到连接关闭的时候，会通知该线程
                while (!dm.dead) {
                    wait();
                    System.out.println("dead exit");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }


    static class StreamWriter extends Thread {
        OutputStream os;
        InputStream is;

        StreamWriter(InputStream io, OutputStream os ) {
            this.is = io;
            this.os = os;
            start();
        }

        @Override
        public void run() {
            byte[] b = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 监控znode的exists状态，并触发相关行为，
     * 如果节点创建， 则会执行指定的命令。然后将znode中的数据写入到指定的文件中。
     * 在第一次启动时，会检查znode是否存在，并触发相关动作。
     * @param args
     */
    public static void main(String[] args) {

//        if (args.length < 4) {
//            System.err
//                    .println("USAGE: Executor hostPort znode filename program [args ...]");
//            System.exit(2);
//        }

        String hostPort = "0.0.0.0:2181";
        String znode = "/henvealf/test1";
        String filename = "a.txt";
        String exec[] = new String[]{"ping"};

        try {
            new Executor(hostPort, znode, filename, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
