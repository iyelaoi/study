package cn.wqz.study.zookeeper.sample.curator.nameserver;

import org.junit.Test;

public class PeerNodeTester {

    @Test
    public void testGetPeerNode(){
        PeerNode peerNode = PeerNode.getInstance();
        long id = peerNode.getId();
        System.out.println("id = " + id);
        id = peerNode.getId();
        System.out.println("id = " + id);
    }
}
