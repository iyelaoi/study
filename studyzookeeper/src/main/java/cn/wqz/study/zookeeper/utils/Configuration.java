package cn.wqz.study.zookeeper.utils;

public interface Configuration {
    String ZOOKEEPER_SERVER_CONNECTION_IP_PORT = "127.0.0.1:2181";
    String NAME_SERVER_ROOT_PATH = "/name-root";
    String NAME_SERVER_PATH_PREFIX = NAME_SERVER_ROOT_PATH + "/PeerNode-";
    String TEST_NODE_PATH = "/test/demo";
}
