package cn.wqz.algorithms.recursion;

import java.util.Arrays;


/**
 *  递归实现全排列
 *  递归中的问题分解
 *  将原始问题分解为多个子问题，递归求解
 */
public class AllPailie {
    public static void main(String[] args) {

        sort(new int[]{1,2,3,4},3 );
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

    public static void permute(int[] s, int n){


        if (n == 0) {
            /**
             * 每当一路递归至最后一个元素就将排列后的数组打印
             */
            System.out.println(Arrays.toString(s));
        }else{
            /**
             * 将问题进行分解
             * 全排列可分解为最后一个固定，之前的所有元素进行全排列
             * 然后将每个元素与最后一个元素进行互换，同样固定最后一个元素，对前面的元素进行全排列
             */

            /**
             * 首先将原始序列之前的n-1个元素进行递归全排列
             */
            permute(s, n-1);
            int temp = 0;
            for (int i = n-1; i >=0 ; i--) {
                /**
                 * 将原始序列最后一个元素与其他元素互换并进行全排列
                 */
                temp = s[n];
                s[n] = s[i];
                s[i] = temp;
                permute(s, n-1);

                /**
                 * 每对一个元素互换并排列后，将原始元素换回，以供与其他元素互换
                 */
                temp = s[n];
                s[n] = s[i];
                s[i] = temp;
            }
        }

    }

}
