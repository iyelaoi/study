package cn.wqz.algorithms.sorts;

import java.util.Arrays;

/**
 * 堆排序
 * 1. 构建大根堆
 * 2. 将堆顶元素与末尾元素进行交换
 * 3. 调整堆，每次将最大元素（不包括之前的交换元素）调整到堆顶，并与后边的元素进行交换
 * （借助全局变量 len,每交换一次，说明后面多了一个有序的大元素，将len减一，当调整堆时，使减过的部分不参与调整）
 */
public class HeapSort {

    static int len;

    public static void main(String[] args) {
        int[] data = DataSet.data;
        sort(data);
        System.out.println(Arrays.toString(data));
    }

    static void sort(int[] arr){
        len = arr.length;
        build(arr);
        for (int i = arr.length - 1; i > 0; i--){
            swap(arr, 0, i);
            len--;
            heapFy(arr, 0);
        }
    }

    /**
     * 构建大根堆
     * @param arr
     */
    static void build(int[] arr){
        /**
         * 从1/2处开始
         */
        for (int i = (len/2); i >= 0 ; i--) { // 一定要从后往前，否则不能将下面的大元素提上去
            heapFy(arr, i);
        }
    }

    static void heapFy(int[] arr, int i){
        int left = 2*i + 1;
        int right = 2*i + 2;
        int lagest = i;
        if(left < len && arr[left] > arr[lagest] ){
            lagest = left;
        }

        if (right < len && arr[right] > arr[lagest]){
            lagest = right;
        }

        if (lagest != i){
            swap(arr, lagest, i);
            heapFy(arr, lagest); //
        }


    }

    static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


}
