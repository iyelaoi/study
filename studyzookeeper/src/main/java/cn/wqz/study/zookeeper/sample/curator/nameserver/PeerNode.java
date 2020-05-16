package cn.wqz.study.zookeeper.sample.curator.nameserver;

import cn.wqz.study.zookeeper.utils.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 集群节点命名服务
 * @author 魏启壮
 */
public class PeerNode {
    private CuratorFramework client = null;
    private String pathRegistered = null;
    private static volatile PeerNode singleInstance = null;

    private PeerNode(){}

    /**
     * 会出现不安全发布对象的情况
     * @return
     */
    public static PeerNode getInstance(){
        if(null == singleInstance){
            synchronized (PeerNode.class){
                if(null == singleInstance){
                    singleInstance = new PeerNode();
                    singleInstance.client = CuratorFrameworkFactory
                            .newClient(Configuration.ZOOKEEPER_SERVER_CONNECTION_IP_PORT, new ExponentialBackoffRetry(1000, 3));
                    singleInstance.client.start();
                    singleInstance.init();
                }
            }
        }
        return singleInstance;
    }

    public void init(){
        try{
            client.create()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(Configuration.NAME_SERVER_ROOT_PATH, "name server root node".getBytes("UTF-8"));
            pathRegistered = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(Configuration.NAME_SERVER_PATH_PREFIX);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public long getId(){
        String sid = null;
        if(null == pathRegistered){
            throw new RuntimeException("node register failure");
        }
        int index = pathRegistered.lastIndexOf(Configuration.NAME_SERVER_PATH_PREFIX);
        if(index > -1){
            index += Configuration.NAME_SERVER_PATH_PREFIX.length();
            sid = index <= pathRegistered.length() ? pathRegistered.substring(index) : null;
        }
        if(null == sid){
            throw new RuntimeException("distribute node error");
        }
        return Long.parseLong(sid);
    }
}
