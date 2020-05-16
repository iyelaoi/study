package cn.wqz.study.zookeeper.sample.curator.lock;

import cn.wqz.study.zookeeper.sample.curator.ClientFactory;
import cn.wqz.study.zookeeper.utils.concurrent.FutureTaskScheduler;
import jdk.nashorn.internal.runtime.ECMAException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;

public class ZKLockTester {
    int count = 0;
    @Test
    public void testLock()throws  InterruptedException{
        for (int i = 0; i < 10; i++) {
            FutureTaskScheduler.add(()->{
                ZKLock zkLock = new ZKLock();
                zkLock.lock();
                for (int j = 0; j < 10; j++) {
                    count++;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("count = " + count);
                zkLock.unlock();
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testzkMutex() throws InterruptedException {
        CuratorFramework client = ClientFactory.getAndStartClient();
        final InterProcessMutex mutex = new InterProcessMutex(client, "/mutex");
        for (int i = 0; i < 10; i++) {
            FutureTaskScheduler.add(()->{
                try {
                    mutex.acquire();
                    for (int j = 0; j < 10; j++) {
                        count++;
                    }
                    Thread.sleep(1000);
                    System.out.println("count = " + count);
                    mutex.release();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}
