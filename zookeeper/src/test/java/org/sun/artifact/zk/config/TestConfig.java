package org.sun.artifact.zk.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestConfig {

    private ZooKeeper zk;

    @BeforeEach
    public void connect() {
        zk = ZkUtils.getZk();
    }

    @AfterEach
    public void close() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConf() {

        WatcherCallback watcherCallback = new WatcherCallback();
        watcherCallback.setZk(zk);
        MyConf myConf = new MyConf();
        watcherCallback.setConf(myConf);

        zk.exists("/AppConf", watcherCallback, watcherCallback, "abc");

        watcherCallback.await();

        System.out.println(myConf.getConf());
    }
}
