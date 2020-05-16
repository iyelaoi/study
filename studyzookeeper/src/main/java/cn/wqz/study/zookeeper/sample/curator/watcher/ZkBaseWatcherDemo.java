package cn.wqz.study.zookeeper.sample.curator.watcher;

import cn.wqz.study.zookeeper.sample.curator.ClientFactory;
import cn.wqz.study.zookeeper.utils.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.Test;

public class ZkBaseWatcherDemo {

    String workerPath = "/test/listener/remoteNode";
    String subWorkerPath = "/test/listener/remoteNode/id-";

    @Test
    public void testWatcher() throws Exception {
        CuratorFramework client = ClientFactory.createSimple(Configuration.ZOOKEEPER_SERVER_CONNECTION_IP_PORT);
        client.start();
        if(null==client.checkExists().forPath(workerPath)){
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(workerPath);
        }

        try{
            Watcher w = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("watcher = " + watchedEvent);
                }
            };
            // 仅注册一次
            byte[] content = client.getData().usingWatcher(w).forPath(workerPath);
            System.out.println("the content of the node listened: " + new String(content));
            client.setData().forPath(workerPath, "first modify".getBytes());

            client.setData().forPath(workerPath, "secend modify".getBytes());
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
