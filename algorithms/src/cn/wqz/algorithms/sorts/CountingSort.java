package cn.wqz.algorithms.sorts;

import java.util.Arrays;

/**
 * 计数排序
 * 不是基于比较的排序算法
 * 目标数据是有明确的范围的整数
 */
public class CountingSort {
    public static void main(String[] args) {
        int[] data = {2,2,1,1,6,6,4,3};
        sort(data,6); // 注意边界问题
        System.out.println(Arrays.toString(data));

    }

    /**
     *
     * @param arr
     * @param range
     */
    static void sort(int[] arr, int range){
        int[] counts = new int[range+1];
        for (int i = 0; i < arr.length; i++){
            counts[arr[i]]++;
        }
        int index = 0;
        for (int i = 0;i < counts.length;i++){
            while(counts[i]-- > 0){
                arr[index++] = i;
            }
        }
    }
}
