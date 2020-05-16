package cn.wqz.algorithms.sorts;

import java.util.Arrays;

/**
 * 冒泡排序
 *
 */
public class BubbleSort {

    static int count = 0;
    public static void main(String[] args) {
        int[] arr = DataSet.data;
        sort(arr);
        System.out.println("compare count：" + count);
        System.out.println(Arrays.toString(arr));

    }

    static void sort(int[] arr){
        int temp = 0;
        boolean over = true;

        for (int i = 0; i < arr.length-1; i++) {
            over = true;
            /**
             * 每次循环都会将一个最大的移到最后
             */
            for (int j = 0; j < arr.length-1-i; j++) {
                count++;
                if (arr[j]>arr[j+1]){
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    /**
                     * 使用标志位，当一次遍历中所有的元素都不需要交换，直接结束排序
                     */
                    over = false;
                }
            }
            if (over){
                return;
            }
        }
    }
}
