package cn.wqz.algorithms.sorts;

import java.util.Arrays;

/**
 *
 */
public class MergeSort {


    static int count = 0;
    public static void main(String[] args) {
        int[] arr = DataSet.data;
        sort(arr,0, arr.length-1);
        System.out.println("compare count：" + count);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 需要额外的内存空间
     * @param arr
     * @param start
     * @param end
     */
    static void sort(int[] arr, int start, int end){
        count++;
        if (start >= end){
            return;
        }
        int mid = (start + end)/2;
        sort(arr, start, mid);
        sort(arr, mid+1, end);
        int[] tempArr = merge(arr, start, mid, end);
        int k = 0;
        for (int i = start; i <= end; i++){
            arr[i] = tempArr[k++];
        }
    }

    static int[] merge(int[] arr, int start,int mid, int end){
        int[] result = new int[end-start+1];
        int i = start;
        int j = mid + 1;
        int index = 0;
        while(i <= mid && j <= end){
            result[index++] = arr[i] > arr[j] ? arr[j++] : arr[i++];
        }
        while (i <= mid){
            result[index++] = arr[i++];
        }
        while(j <= end){
            result[index++] = arr[j++];
        }
        System.out.println(Arrays.toString(result));
        return result;
    }
}
