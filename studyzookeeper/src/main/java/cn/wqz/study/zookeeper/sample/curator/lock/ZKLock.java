package cn.wqz.study.zookeeper.sample.curator.lock;

import cn.wqz.study.zookeeper.utils.Configuration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式项目编码应当注意接口调用次数的问题
 * 能一次远程调用绝不进行两次调用
 * 框架中可能有些一次调用的方法你不知道
 * 但也可能是框架本身就没有提供这种方式
 * 该方式也需要服务端的支持
 * 如判断和删除：
 * if(null != client.checkExists().forPath(path))
 *      client.delete().forPath(path);
 * 两次远程调用？服务端支持一次吗？
 * 如果支持，很合理
 * 如果不支持，Curator缓存支持吗？在缓存中判断，删除操作进行远程调用
 */
public class ZKLock implements Lock{
    private static final String ZK_PATH = "/test/lock";
    private static final String LOCK_PREFIX = ZK_PATH + "/";
    private static long WAIT_TIME = 1000;

    private CuratorFramework client = null;
    private String locked_short_path = null;
    private String locked_path = null;
    private String prior_path = null;
    private final AtomicInteger lockCount = new AtomicInteger(0);
    private Thread thread;

    public ZKLock(){
        client = CuratorFrameworkFactory.newClient(Configuration.ZOOKEEPER_SERVER_CONNECTION_IP_PORT,
                new ExponentialBackoffRetry(1000,3));
        client.start();
        try {
            if(null == client.checkExists().forPath(ZK_PATH)){
                // 并发创建节点问题，会产生线程安全
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(ZK_PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean lock() {
        synchronized (this){
            if(lockCount.get() == 0){
                thread = Thread.currentThread();
                lockCount.incrementAndGet();
            }else{
                if(!thread.equals(Thread.currentThread())){ // 不是同一线程，不能够获得锁
                    return false;
                }
                // 是同一线程，可以重复获得锁
                lockCount.incrementAndGet();
                return true;
            }
        }
        try{
            boolean locked = false;
            locked = tryLock();
            if(locked){
                return true;
            }
            while(!locked){
                await();
                List<String> waiters = getWaiters();
                if(checkLocked(waiters)){
                    locked = true;
                }
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
            unlock();
        }
        return false;
    }

    @Override
    public boolean unlock() {
        // 只有加锁的线程才能解锁
        if(!thread.equals(Thread.currentThread())){
            return false;
        }
        int newLockCount = lockCount.decrementAndGet();
        if(newLockCount < 0){
            throw new IllegalMonitorStateException("计数不对：" + locked_path);
        }
        if(newLockCount != 0){
            return true;
        }
        try{
            if(null != client.checkExists().forPath(locked_path)){
                client.delete().forPath(locked_path);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean tryLock()throws Exception{
        // 1. 创建新的节点
        locked_path = client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(LOCK_PREFIX);
        if(null == locked_path){
            throw new Exception("zk error");
        }
        // 截取节点编码
        locked_short_path = getShorPath(locked_path);

        // 2. 获取锁根节点上的所有子节点
        List<String> waiters = getWaiters();

        // 判断当前节点编号是否是最小
        if(checkLocked(waiters)){
            // 是最小的则加锁成功
            return true;
        }
        // 不是最小的，监听当前节点的前一个节点
        int index = Collections.binarySearch(waiters,locked_short_path);
        if(index < 0){
            // 网络抖动 获取到的子节点列表里没有自己了
            throw new Exception("节点里没有找到自己：" + locked_short_path);
        }

        // 如果没有获得锁
        // 保存前一个节点，供监听
        prior_path = ZK_PATH + "/" + waiters.get(index - 1);
        return false;
    }

    /**
     * 根据路径取得编号
     * @param locked_path
     * @return
     */
    private String getShorPath(String locked_path) {
        int index = locked_path.lastIndexOf(ZK_PATH + "/");
        if (index >= 0) {
            index += ZK_PATH.length() + 1;
            return index <= locked_path.length() ? locked_path.substring(index) : "";
        }
        return null;
    }

    /**
     * 从zookeeper中拿到ZK_PATH的所有子节点
     */
    protected List<String> getWaiters() {
        List<String> children = null;
        try {
            children = client.getChildren().forPath(ZK_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return children;
    }


    private boolean checkLocked(List<String> waiters) {

        //节点按照编号，升序排列
        Collections.sort(waiters);

        // 如果是第一个，代表自己已经获得了锁
        if (locked_short_path.equals(waiters.get(0))) {
            System.out.println("成功的获取分布式锁,节点为:" + locked_short_path);
            return true;
        }
        return false;
    }

    private void await()throws Exception{
        if(null == prior_path){
            throw new Exception("prior_path error");
        }
        final CountDownLatch latch = new CountDownLatch(1);
        Watcher w = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("node changed watchedEvent = " + watchedEvent);
                if(watchedEvent.getType().equals(Event.EventType.NodeDeleted)){
                    System.out.println("[delete]: path = " + watchedEvent.getPath() +
                            ", state = " + watchedEvent.getState());
                    latch.countDown();
                }
            }
        };
        client.getData().usingWatcher(w).forPath(prior_path);
        latch.await(WAIT_TIME, TimeUnit.SECONDS);
    }
}
