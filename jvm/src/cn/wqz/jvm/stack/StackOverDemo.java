package cn.wqz.jvm.stack;

public class StackOverDemo {
    static int num = 0;
    public static void main(String[] args) {
        try{
            func();
        }catch (Throwable throwable){
            System.out.println(throwable);
            System.out.println(num);
        }

    }

    /**
     * StackOverflowError
     */
    static void func(){
        num++;
        func();
    }


}
