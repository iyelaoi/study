package cn.wqz.algorithms.recursion;


/**
 * 阶乘
 */
public class Fact {

    public static void main(String[] args) {
        System.out.println(fact1(4));
        System.out.println(fact2(4));
    }

    /**
     * 递归实现
     * @param n
     * @return
     */
    private static int fact1(int n){
        if(n == 1){
            return 1;
        }else{
            return n * fact1(n-1);
        }
    }

    /**
     * 非递归实现
     * @param n
     * @return
     */
    private static int fact2(int n){
        int result = 1;
        while(n > 1){
            result *= n--; // 其实n--也就相当于栈的pop
        }
        return result;
    }



}
