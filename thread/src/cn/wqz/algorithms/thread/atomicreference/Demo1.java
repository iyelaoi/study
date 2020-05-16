package cn.wqz.algorithms.thread.atomicreference;

import cn.wqz.algorithms.thread.model.Student;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 我始终没有明白将自定义类包装为AtomicReference的意义何在
 *
 * Atomic 应该为多线程的资源共享问题提供一定的安全保障措施
 *
 * 模拟环境：
 * 多线程操作共享资源： 多线程更改共享资源（学生）的属性
 *
 * 该学生在学校非常活跃，参加各种学校活动，每参加一项活动，该活动负责人会对该学生进行加分
 *
 * 由于参加活动很多，多个活动负责人可能同时为该学生进行加分
 */
public class Demo1{

    /**
     * 线程共享学生对象
     */
    static Student student = new Student("sa17225389", "wqz", 20, 0);
    static Student student2 = new Student("sa17225331", "wwqz", 20, 0);
    static CountDownLatch countDownLatch = new CountDownLatch(10);

    private static class StudentActionHandler implements Runnable{
        Student student;
        StudentActionHandler(Student student){
            this.student = student;
        }
        @Override
        public void run() {
            System.out.println(student);
        }
    }
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new StudentActionHandler(student));
    public static void main(String[] args) throws InterruptedException {

        // 假设参加10个活动
        for(int i = 0; i < 10; i++){
            new Thread(()->{

                int n = 1000;
                while(n-- > 0){
                    // 每个活动加 1 分
                    student.setScore(student.getScore()+1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " await over");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println(student);

    }
}
