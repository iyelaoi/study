package cn.wqz.study.zookeeper.sample.curator.watcher;

import cn.wqz.study.zookeeper.sample.curator.ClientFactory;
import cn.wqz.study.zookeeper.utils.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class ZkTreeCacheWatcherDemo {
    String workerPath = "/test/listener/remoteNode";
    String subWorkerPath = "/test/listener/remoteNode/id-";

    @Test
    public void testWatcher() throws Exception {
        CuratorFramework client = ClientFactory.createSimple(Configuration.ZOOKEEPER_SERVER_CONNECTION_IP_PORT);
        client.start();
        if(null==client.checkExists().forPath(workerPath)){
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(workerPath);
        }

        try {
            TreeCache treeCache = new TreeCache(client, workerPath);
            TreeCacheListener treeCacheListener = new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                    ChildData childData = treeCacheEvent.getData();
                    if(childData == null){
                        // 监听器总会收到一个INITIALIZED的事件，childData为null
                        System.out.println("event type: " + treeCacheEvent.getType() + ": childData id null");
                        return;
                    }
                    switch(treeCacheEvent.getType()){
                        case NODE_ADDED:
                            System.out.println("【TreeCache】add child node: path = " + childData.getPath() +
                                    ", data = " + new String(childData.getData(), "UTF-8"));
                            break;
                        case NODE_UPDATED:
                            System.out.println("【TreeCache】update child node: path = " + childData.getPath() +
                                    ", data = " + new String(childData.getData(), "UTF-8"));
                            break;
                        case NODE_REMOVED:
                            System.out.println("【TreeCache】remove child node: path = " + childData.getPath() +
                                    ", data = " + new String(childData.getData(), "UTF-8"));
                            break;
                        default:
                            break;
                    }
                }
            };
            treeCache.getListenable().addListener(treeCacheListener);
            treeCache.start();
            Thread.sleep(1000);

            for (int i = 0; i < 3; i++) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(subWorkerPath+i);
            }

            Thread.sleep(1000);
            for (int i = 0; i < 3; i++) {
                client.delete().forPath(subWorkerPath+i);
            }
            client.delete().forPath(workerPath);
            Thread.sleep(Integer.MAX_VALUE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
