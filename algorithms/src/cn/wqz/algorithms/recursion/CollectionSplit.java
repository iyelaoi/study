package cn.wqz.algorithms.recursion;


/**
 * 集合的划分
 */
public class CollectionSplit {
    public static void main(String[] args) {

        int n = 10;
        int k = 2;

        System.out.println(split(n,k));
    }



    public static void split1(){
        
    }

    /**
     * 统计划分方式数
     * @param n 元素个数
     * @param k 划分集合的个数
     * @return
     */
    public static int split(int n, int k){
        if(k==1 || n == k){
            return 1;
        }
        if(n < k || k ==0 ) {
            return 0;
        }
        /**
         * 某一个元素为单个元素的子集
         * 则还剩 n-1 个元素划分为 k 个子集
         */
        int t = split(n-1,k-1);

        /**
         * 某个元素为子集中的一个元素
         * 则将该元素拿出，还剩n-1个元素划分为n个集合
         */
        return t + split(n-1,k)*k;
    }


}
