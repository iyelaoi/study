package cn.wqz.study.zookeeper.sample.curator.lock;

/**
 * 锁接口
 */
public interface Lock {

    /**
     * 加锁
     * @return
     */
    boolean lock();

    /**
     * 释放锁
     * @return
     */
    boolean unlock();
}
