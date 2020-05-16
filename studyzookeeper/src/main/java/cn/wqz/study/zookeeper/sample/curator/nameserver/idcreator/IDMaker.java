package cn.wqz.study.zookeeper.sample.curator.nameserver.idcreator;

import cn.wqz.study.zookeeper.sample.curator.ClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * ID生成器
 */
public class IDMaker {

    CuratorFramework client = null;

    public void init(){
        client = ClientFactory.createSimple("127.0.0.1:2181");
        client.start();
    }

    public void destroy(){
        if(null != client){
            client.close();
        }
    }

    private String createSeqNode(String pathPrefix){
        try{
            String destPath = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(pathPrefix);
            return destPath;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建ID
     * @param nodeName
     * @return
     */
    public String makeId(String nodeName){
        String str = createSeqNode(nodeName);
        if(null == str){
            return null;
        }
        int index = str.lastIndexOf(nodeName);
        if(index > -1){
            index += nodeName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

}
