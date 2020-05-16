package cn.wqz.algorithms.sorts;

import java.util.Arrays;

/**
 * 选择排序
 * 不稳定
 */
public class SelectSort {

    static int count = 0;
    public static void main(String[] args) {
        int[] arr = DataSet.data;
        sort(arr);
        System.out.println("compare count：" + count);
        System.out.println(Arrays.toString(arr));
    }

    static void sort(int[] arr){
        int temp = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i+1; j < arr.length; j++) {
                count++;
                if (arr[i] > arr[j]){
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}
