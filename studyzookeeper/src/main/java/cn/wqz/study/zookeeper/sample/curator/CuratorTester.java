package cn.wqz.study.zookeeper.sample.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

@Slf4j
public class CuratorTester {


    CuratorFramework client = ClientFactory.createSimple("127.0.0.1:2181");
    String zkPath = "/test/CRUD/node-1";

    /**
     * 创建节点
     * @throws Exception
     */
    @Test
    public void createNode(){
        try {
            client.start();
            String data = "wqz";
            byte[] payload = data.getBytes("UTF-8");
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void readNode(){
        client.start();
        try{
            Stat stat = client.checkExists().forPath(zkPath);
            if(null != stat){
                byte[] payload = client.getData().forPath(zkPath);
                String data = new String(payload, "UTF-8");
                System.out.println("read data:" + data);
                String parentPath = "/test";
                List<String> children = client.getChildren().forPath(parentPath);
                for(String child : children){
                    System.out.println("child:" + child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void updateNode(){
        try{
            client.start();
            String data = "wwqz";
            byte[] payload = data.getBytes("UTF-8");
            client.setData()
                    .forPath(zkPath, payload);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void updateNodeAsync(){
        try{
            AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {
                @Override
                public void processResult(int i, String s, Object o, String s1) {
                    System.out.println(
                        "i = " + i + " | " +
                        "s = " + s + " | " +
                        "o = " + o + " | " +
                        "s1 = " + s1
                    );
                }
            };
            client.start();
            String data = "new wwqz";
            byte[] payload = data.getBytes("UTF-8");
            client.setData()
                    .inBackground(callback)
                    .forPath(zkPath, payload);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void deleteNode(){
        try{
            client.start();
            client.delete().forPath(zkPath);
            List<String> list = client.getChildren().forPath("/test");
            for(String child : list){
                log.info("child:" + child);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }

    }
}
