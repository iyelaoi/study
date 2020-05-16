package cn.wqz.algorithms.sorts;

import java.util.Arrays;

public class QuickSort {

    static int count = 0;
    public static void main(String[] args) {
        int[] arr = DataSet.data;
        sort(arr,0, arr.length-1);
        System.out.println("compare count：" + count);
        System.out.println(Arrays.toString(arr));
    }

    static void sort(int[] arr, int start, int end){
        count++;
        int partitionIndex;
        if (start < end){
            partitionIndex = partition(arr, start, end);
            sort(arr, start, partitionIndex-1); // 在每个递归调用中都对目标数据进行了操作
            sort(arr, partitionIndex, end); // 所以返回值什么的无所谓，这属于直接操纵数据集类型的递归
        }

    }

    /**
     * partition实现方式有很多
     * 此方法使用的是将目标元素靠近自己
     * 也可以使用与基准元素交换实现（容易理解）
     * @param arr
     * @param start
     * @param end
     * @return
     */
    static int partition(int[] arr, int start, int end){
        int pivot = end;
        int partitionIndex = end-1;
        for(int i = partitionIndex; i >= start; i--){
            if (arr[i] > arr[pivot]){ // 每碰到一个大的，就将其移动到靠近基准的位置
                swap(arr, i, partitionIndex);
                partitionIndex--; // 移动索引
            }
        }
        swap(arr, pivot, partitionIndex+1); // 将基准与边界元素交换，实现基准的左右划分
        return partitionIndex+1;
    }

    static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
