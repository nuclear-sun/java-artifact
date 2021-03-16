package org.sun.artifact.zk.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatcherCallback implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    private ZooKeeper zk;

    private MyConf conf;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public void await() {

        zk.exists("/AppConf", this, this, "abc");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void processResult(int i, String s, Object o, byte[] data, Stat stat) {

        if(data != null) {
            String str = new String(data);
            conf.setConf(str);
            countDownLatch.countDown();
        }
    }

    public void processResult(int i, String s, Object o, Stat stat) {

        if(stat != null) {
            zk.getData("/AppConf", this, this, "context");
        }

    }

    public void process(WatchedEvent event) {

        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                break;
            case NodeDataChanged:
                zk.getData("/AppConf", this, this, "context");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }

    }
}
