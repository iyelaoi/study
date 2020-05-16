package cn.wqz.algorithms.recursion;


/**
 * 二分查找
 * 递归中的二分问题，将问题划分为多个子问题，只筛选部分子问题进行求解
 */
public class BinarySearch {
    public static void main(String[] args) {
        int[] x = {1,2,3,4,5,6,7,8,9};
        System.out.println(search(x, 4, 0,8 ));

    }

    /**
     * 将目标有序数组分为两个部分，然后只选择其中一个分支进行查找
     * @param l 数据集
     * @param x 需要在数据集中查找的目标元素
     * @param i 起始查找索引
     * @param j 末尾查找索引
     */
     private static int search(int[] l, int x, int i, int j) {

         if (i > j) { // 起始大于末尾
             return -1;
         } else if (i == j) { // 当起始等于末尾
             if (l[i] == x) {
                 return i;
             } else {
                 return -1;
             }
         } else {
             int mid = (i + j) / 2;
             if (x == l[mid]) {
                 return mid;
             } else if (x > l[mid]) { // 目标元素大于中间的元素，在右面查找
                 return search(l, x, mid + 1, j);
             } else { // 否则在左面查找
                 return search(l, x, i, mid - 1);
             }
         }

     }
}
