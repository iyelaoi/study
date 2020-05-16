package cn.wqz.algorithms.recursion;

/**
 * 阶乘
 */
public class Factorial {

    public static void main(String[] args) {
        System.out.println(factorial(3));
        System.out.println(factorial2(3));
    }

    /**
     * 递归实现
     * @param n
     * @return
     */

    public static int factorial(int n){
        if(n==1){
            return 1;
        }
        return n * factorial(n-1);
    }

    /**
     * 非递归实现
     * @param n
     * @return
     */
    public static int factorial2(int n){
        int result = 1;
        while(n > 0){
            result = result * n;
            n--;
        }
        return result;
    }

}
