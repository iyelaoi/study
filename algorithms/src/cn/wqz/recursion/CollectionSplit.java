package cn.wqz.recursion;


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
     * @param n
     * @param k
     * @return
     */
    public static int split(int n, int k){
        if(n == 1 || k==1){
            return 1;
        }

        if(n <= k ) {
            return 1;
        }

        // 某一个元素为单个元素的子集
        int t = split(n-1,k-1);

        // 某个元素为子集中的一个元素
        return t + split(n-1,k)*k;
    }


}
