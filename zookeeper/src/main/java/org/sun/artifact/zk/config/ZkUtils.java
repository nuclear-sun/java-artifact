package org.sun.artifact.zk.config;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkUtils {

    private static ZooKeeper zk;

    private static final String address = "192.168.1.102:2181/testConf";

    private static final DefaultWatcher watcher = new DefaultWatcher();

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static ZooKeeper getZk() {
        try {
            zk = new ZooKeeper(address, 1000, watcher);
            watcher.setCc(countDownLatch);
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return zk;
    }
}
