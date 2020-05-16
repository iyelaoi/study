package cn.wqz.study.zookeeper.sample.javaAPI.details;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.util.Log4jConfigurer;

/**
 * 这个测试类测试在指定了默认watcher，并且有不止一个watcher实例的情况下。zkClient对Event-NONE事件的响应机制。
 * servers：192.168.61.129:2181，192.168.61.130:2181，192.168.61.132:2181<br>
 * 我们选择一个节点进行连接(192.168.61.129)，这样好在主动停止这个zk节点后，观察watcher的响应情况。
 * @author yinwenjie
 */
public class TestEventNoneWatcher implements Runnable {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
            System.exit(-1);
        }
    }

    /**
     * 日志
     */
    private static final Log LOGGER = LogFactory.getLog(TestEventNoneWatcher.class);

    private ZooKeeper zkClient = null;

    public static void main(String[] args) throws Exception {
        TestEventNoneWatcher testEventNoneWatcher = new TestEventNoneWatcher();
        new Thread(testEventNoneWatcher).start();
    }

    public void run() {
        /*
         * 验证过程如下：
         * 1、连接zk后，并不进行进行默认的watcher的注册，并且使用exist方法注册一个监听节点"X"的监听器。
         *      （完成后主线程进入等待状态）
         * 2、关闭192.168.61.129:2181这个zk节点，让Disconnected事件发生。
         *      观察到底是哪个watcher响应这些None事件。
         * */
        //1、========================================================
        //注册默认监听
        EventNodeWatcherDefault watcherDefault = new EventNodeWatcherDefault(this);
        try {
            this.zkClient = new ZooKeeper("192.168.61.129:2181", 120000, watcherDefault);
        } catch (IOException e) {
            TestEventNoneWatcher.LOGGER.error(e.getMessage(), e);
            return;
        }

        String path = "/X";
        EventNodeWatcherOne eventNodeWatcherOne = new EventNodeWatcherOne(this.zkClient , path);
        //注册监听，注意，这里两次exists方法的执行返回都是null，因为“X”节点还不存在
        try {
            zkClient.exists(path, eventNodeWatcherOne);
            //创建"X"节点，为了简单起见，我们忽略权限问题。
            //并且创建一个临时节点，这样重复跑代码的时候，不用去server上手动删除)
            zkClient.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            TestEventNoneWatcher.LOGGER.error(e.getMessage(), e);
            return;
        }

        //完成注册后，主线程等待。然后关闭192.168.61.129上的zk节点
        synchronized(this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                TestEventNoneWatcher.LOGGER.error(e.getMessage(), e);
                System.exit(-1);
            }
        }
    }

    public ZooKeeper getZkClient() {
        return zkClient;
    }
}

/**
 * 这是默认的watcher实现。
 * @author yinwenjie
 */
class EventNodeWatcherDefault implements Watcher {
    /**
     * 日志
     */
    private static Log LOGGER = LogFactory.getLog(EventNodeWatcherDefault.class);

    private TestEventNoneWatcher eventNoneWatcherThead;

    public EventNodeWatcherDefault(TestEventNoneWatcher eventNoneWatcherThead) {
        this.eventNoneWatcherThead = eventNoneWatcherThead;
    }

    public void process(WatchedEvent event) {
        //重新注册监听
        this.eventNoneWatcherThead.getZkClient().register(this);

        KeeperState keeperState = event.getState();
        EventType eventType = event.getType();
        EventNodeWatcherDefault.LOGGER.info("=========默认EventNodeWatcher监听到None事件：keeperState = "
                + keeperState + "  :  eventType = " + eventType);
    }
}

/**
 * 这是第一种watcher
 * @author yinwenjie
 */
class EventNodeWatcherOne implements Watcher {
    /**
     * 日志
     */
    private static Log LOGGER = LogFactory.getLog(EventNodeWatcherOne.class);

    private ZooKeeper zkClient;

    /**
     * 被监控的znode地址
     */
    private String watcherPath;

    public EventNodeWatcherOne(ZooKeeper zkClient , String watcherPath) {
        this.zkClient = zkClient;
        this.watcherPath = watcherPath;
    }

    public void process(WatchedEvent event) {
        try {
            this.zkClient.exists(this.watcherPath, this);
        } catch (Exception e) {
            EventNodeWatcherOne.LOGGER.error(e.getMessage(), e);
        }
        KeeperState keeperState = event.getState();
        EventType eventType = event.getType();

        EventNodeWatcherOne.LOGGER.info("=========EventNodeWatcherOne监听到事件：keeperState = "
                + keeperState + "  :  eventType = " + eventType);
    }
}
