package cn.wqz.jvm.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 自己编写时注意代码规范
 */
class Student{
    static int num = 1;
    String id;
    String name;
    Student(){
        this.id = "id" + num;
        this.name = "name" + num;
        num++;
    }
}

/**
 * 如果Student被回收，无法知道ReferenceQueue中的Reference包装的是哪个Student
 * 使用此种方式，定义一个属性，供查看
 */
class StudentSoftReference extends SoftReference<Student>{

    public StudentSoftReference(Student referent) {
        super(referent);
        id = referent.id;
    }

    public StudentSoftReference(Student referent, ReferenceQueue<? super Student> q) {
        super(referent, q);
        id = referent.id;
    }

    /**
     * 此属性在目标Student对象被回收后，reference对象中可以知道自己包装的对象信息
     */
    final String id;

}


/**
 * 软引用实现内存敏感型缓存
 */
public class SoftReferenceCache {

    /**
     * 注意： 此种方式发布的对象并不安全（对象溢出）
     * 可能没有初始化完成就被使用
     */
    static HashMap<String, StudentSoftReference> cache = new HashMap<>();
    static ReferenceQueue<Student> referenceReferenceQueue = new ReferenceQueue<>();
    static Student[] students = new Student[20];
    static {
        for(int i = 0; i < students.length; i++){
            students[i] = new Student();
        }
    }

    /**
     * 向缓存中插入数据
     * @param student
     * @return
     */
    public static StudentSoftReference put(Student student){
        return cache.put(student.id, new StudentSoftReference(student, referenceReferenceQueue));
    }

    /**
     * 检查引用队列
     * 非线程安全
     * @return
     */
    public static int checkReferenceQueue(){
        StudentSoftReference reference = null;
        int count = 0;
        // 从队列中取出已经被垃圾回收的引用对象
        while((reference = (StudentSoftReference) referenceReferenceQueue.poll()) != null){
            String id = reference.id;
            cache.remove(id);
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < students.length; i++){
            put(students[i]);
        }
        Thread.sleep(10000);
        checkReferenceQueue();
        System.out.println(cache);
    }
}
