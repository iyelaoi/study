package cn.wqz.study.zookeeper.sample.curator;

import cn.wqz.study.zookeeper.utils.Configuration;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ClientFactory {

    public static CuratorFramework createSimple(String connectionString){
        // 重试策略1s、2s、4s..
        // 最多重试3次
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000,3);
        // 创建CuratorFramework最简单的方式
        return CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
    }

    public static CuratorFramework createWithOption(String connectionString, RetryPolicy retryPolicy,
                                                    int connectionTimeoutMs, int sessionTimeoutMs){

        //return CuratorFrameworkFactory.newClient(connectionString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        // 使用builder的形式创建CuratorFramework实例
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

    public static CuratorFramework getAndStartClient(){
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000,3);
        // 创建CuratorFramework最简单的方式
        CuratorFramework client = CuratorFrameworkFactory.newClient(Configuration.ZOOKEEPER_SERVER_CONNECTION_IP_PORT, retryPolicy);
        client.start();
        return client;
    }

}
