package cn.wqz.algorithms.sorts;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * 基数排序
 *
 */
public class RadixSort {

    public static void main(String[] args) {
        int[] arr = DataSet.getData(100, 1000);
        System.out.println(Arrays.toString(arr));
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    static void sort(int[] arr){
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) max = arr[i];
        }

        int x = 10;
        int wei = 0; // 多少个10
        for (int i = 1; i < 100; i++){
            if (x > max){
                wei = i;
                break;
            }
            x *= 10;
        }
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            temp.add(new ArrayList<>());
        }
        int dev = 1;
        for (int i = 0; i < wei; i++){
            for (int j = 0; j < arr.length; j++){
                int dev1 = arr[j]/dev;
                if (dev1 == 0){
                    temp.get(0).add(arr[j]);
                }else{
                    int mod = dev1 % 10;
                    temp.get(mod).add(arr[j]);
                }
            }
            int index = 0;
            for(ArrayList<Integer> arrayList : temp){
                for (int t : arrayList){
                    arr[index++] = t;
                }
                arrayList.clear();
            }
            dev = dev * 10;
        }
    }
}
