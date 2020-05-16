package cn.wqz.algorithms.recursion;


import java.util.LinkedList;

/**
 * 自然数的递归定义
 * 递归的生成自然数序列
 */
public class NatureNumberDefine {

    public static void main(String[] args) {
        System.out.println(define(10));
    }

    /**
     * 生成第n个自然数
     * @param n
     * @return
     */
    static int define(int n){
        if(n == 1){
            return 1;
        }else{
            return define(n-1) + 1;
        }
    }
}
