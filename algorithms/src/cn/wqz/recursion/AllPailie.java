package cn.wqz.recursion;

import java.util.Arrays;


/**
 *  递归实现全排列
 */
public class AllPailie {
    public static void main(String[] args) {

        sort(new int[]{1,2,3},2 );
    }

    /**
     * 分解成若干个自问体递归
     * 此方法未解决重复元素问题
     * @param s
     * @param n
     */
    public static void sort(int[] s, int n){
        if(n == 0){
            System.out.println(Arrays.toString(s));
        }else{
            sort(s, n-1);
            for(int i = n-1; i >=0; i--){
                int temp = s[n];
                s[n] = s[i];
                s[i] = temp;
                sort(s, n-1);
                temp = s[i];
                s[i] = s[n];
                s[n] = temp;
            }
        }
    }
}
