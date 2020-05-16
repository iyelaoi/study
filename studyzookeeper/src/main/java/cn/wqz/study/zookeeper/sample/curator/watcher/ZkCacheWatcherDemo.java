package cn.wqz.study.zookeeper.sample.curator.watcher;

import cn.wqz.study.zookeeper.sample.curator.ClientFactory;
import cn.wqz.study.zookeeper.utils.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.Test;

public class ZkCacheWatcherDemo {
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

        try {
            NodeCache nodeCache = new NodeCache(client, workerPath,false);
            // 定义监听器
            NodeCacheListener listener = new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    ChildData childData = nodeCache.getCurrentData();
                    System.out.println("ZNode stat change, path = " + childData.getPath());
                    System.out.println("ZNode stat change, data = " + new String(childData.getData(),"UTF-8"));
                    System.out.println("ZNode stat change, stat = " + childData.getStat());
                }
            };
            // 注册监听器
            nodeCache.getListenable().addListener(listener);
            // 开启缓存和监听
            nodeCache.start();

            client.setData().forPath(workerPath, "first modify".getBytes());
            Thread.sleep(1000);

            client.setData().forPath(workerPath, "secend modify".getBytes());
            Thread.sleep(1000);

            client.setData().forPath(workerPath, "third modify".getBytes());
            Thread.sleep(1000);

            client.delete().forPath(workerPath);
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
