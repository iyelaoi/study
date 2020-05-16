package cn.wqz.study.zookeeper.sample.curator.watcher;

import cn.wqz.study.zookeeper.sample.curator.ClientFactory;
import cn.wqz.study.zookeeper.utils.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class ZkPathCacheWatcherDemo {
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
            PathChildrenCache pathChildrenCache = new PathChildrenCache(client, workerPath, true);
            PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    try{
                        ChildData childData = pathChildrenCacheEvent.getData();
                        switch(pathChildrenCacheEvent.getType()){
                            case CHILD_ADDED:
                                System.out.println("add child node: path = " + childData.getPath() +
                                        ", data = " + new String(childData.getData(), "UTF-8"));
                                break;
                            case CHILD_UPDATED:
                                System.out.println("update child node: path = " + childData.getPath() +
                                        ", data = " + new String(childData.getData(), "UTF-8"));
                                break;
                            case CHILD_REMOVED:
                                System.out.println("remove child node: path = " + childData.getPath() +
                                        ", data = " + new String(childData.getData(), "UTF-8"));
                                break;
                            default:
                                break;

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            Thread.sleep(1000);
            for (int i = 0; i < 3; i++) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(subWorkerPath+i);
            }
            Thread.sleep(1000);
            for (int i = 0; i < 3; i++) {
                client.delete().forPath(subWorkerPath+i);
            }
            client.delete().forPath(workerPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
