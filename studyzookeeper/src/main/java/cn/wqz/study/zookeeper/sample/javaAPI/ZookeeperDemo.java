package cn.wqz.study.zookeeper.sample.javaAPI;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * java zookeeper sample
 * 观察者
 */
public class ZookeeperDemo implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static Stat stat = new Stat();

    /**
     * zookeeper客户端事件处理器
     * @param watchedEvent
     */
    public void process(WatchedEvent watchedEvent) {
        System.out.println("ok");
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
            if(Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                countDownLatch.countDown();
            }else if (watchedEvent.getType() == Event.EventType.NodeDataChanged){
                try {
                    // 反复注册
                    System.out.println("配置已修改，新值为："  + new String(zk.getData(watchedEvent.getPath(),true, stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String path = "/test/CRUD/node-1";
        //String path = "/username";
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperDemo());
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.await();
        try {
            System.out.println(new String(zk.getData(path,
                    true/*为true重新注册监听器，还能够继续监听节点事件*/, stat)));
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }


}
