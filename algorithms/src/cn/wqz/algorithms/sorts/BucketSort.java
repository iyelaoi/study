package cn.wqz.algorithms.sorts;

/**
 * 桶排序（假设数据分布均匀）
 * 升级版的计数排序
 * 计数排序每一个数值（多个重复数值算一个）对以一个索引，可能造成很大的空间浪费
 * 使用桶排序门将输入数据按照映射函数映射到相应的桶中，然后对桶内数据进行排序（使用其他的排序算法）
 */
public class BucketSort {

    public static void main(String[] args) {

    }

    /**
     * 假如对数组nums进行桶排序，nums的长度为L，最小元素为A，最大元素为B。
     * gap 为元素间的最大差距
     * gap为(B-A)/L+1，桶的个数为(B-A)/gap+1。
     * int dist = (imax-imin) / n + 1;
     * vector<vector<int> > bucket((imax-imin)/dist + 1);
     * 另外一个重要的性质是，同一个桶中的元素相差最多为gap-1。
     * 对nums中的元素nums[i]，确定放入哪个桶的公式为：(nums[i]-A)/gap
     * @param arr
     */
    static void sort(int[] arr){
        int len = arr.length;
        int max = arr[0];
        int min = arr[0];
        for (int i = 0; i < arr.length; i++){
            if (min > arr[i]){
                min = arr[i];
            }

            if(max < arr[i]){
                max = arr[i];
            }
        }
        int dis = (max - min)/len + 1;
    }
}
