package cn.wqz.algorithms.recursion;


/**
 * 斐波那契数的生成
 */
public class Fibonacci {

    public static void main(String[] args) {
        System.out.println(fib(5));
    }

    /**
     * 递归实现返回第n个斐波那契数
     * 不包含路径
     * @param n
     * @return
     */
    private static int fib(int n){
        if(n < 3){
            return 1;
        }else{
            return fib(n-1) + fib(n-2);
        }

    }
}
