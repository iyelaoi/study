package cn.wqz.algorithms.sorts;

import java.util.Arrays;

/**
 * 插入排序
 */
public class InsertSort {
    static int count = 0;
    public static void main(String[] args) {
        int[] arr = DataSet.data;
        sort(arr);
        System.out.println("compare count：" + count);
        System.out.println(Arrays.toString(arr));
    }


    static void sort(int[] arr){
        int temp = 0;
        for (int i = 1; i < arr.length; i++) {
            temp = arr[i];
            int j = i-1;
            /**
             * 此处不方便用for循环
             * 并且如果有序，此处仅比较一次
             */
            while (j >=0 && temp < arr[j]) {
                count++;
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = temp;
        }
    }
}
