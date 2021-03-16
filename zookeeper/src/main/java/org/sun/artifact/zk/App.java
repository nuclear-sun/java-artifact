package org.sun.artifact.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        final CountDownLatch countDownLatch=  new CountDownLatch(1);

        String address = "192.168.8.200:2181,192.168.8.201:2181,192.168.8.202:2181";
        ZooKeeper zooKeeper = new ZooKeeper(address, 3000, new Watcher() {
            public void process(WatchedEvent event) {

                Event.KeeperState state = event.getState();
                Event.EventType type = event.getType();
                String path = event.getPath();

                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("sync connected.");
                        countDownLatch.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                    case Closed:
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
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
        });


        countDownLatch.await();
        ZooKeeper.States state = zooKeeper.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("connecting...");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("connected.");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        try {
            String path = zooKeeper.create("/sun", "nuclear".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("Path " + path + " created.");

            Stat stat = new Stat();
            byte[] data = zooKeeper.getData(path, new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("get watch event: " + event.toString());
                }
            }, stat);

            System.out.println("received node data: " + new String(data));

            Stat stat1 = zooKeeper.setData(path, "newdata".getBytes(), 0);

            Stat stat2 = zooKeeper.setData(path, "new new data".getBytes(), stat1.getVersion());

            System.out.println("start async getData");
            zooKeeper.getData(path, false, new AsyncCallback.DataCallback() {
                public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                    System.out.println("async getData: " + new String(data));
                }
            }, "ctx");
            System.out.println("end async getData");


        } catch (KeeperException e) {
            e.printStackTrace();
        }

        Thread.sleep(50000);


    }
}
