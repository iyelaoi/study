package cn.wqz.thread.cas;

import cn.wqz.thread.model.Student;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 我始终没有明白对自定义类进行AtomicReference包装的意义何在
 */
public class AtomicReferenceDemo {

    /**
     * 线程共享学生对象
     */
    static Student student = new Student("sa17225389", "wqz", 20, 0);
    static Student student2 = new Student("sa17225331", "wwqz", 20, 0);
    /**
     * 使用原子包装学生对象
     */
    static AtomicReference<Student> atomicReference = new AtomicReference<>(student);

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++){

        }

    }
}
