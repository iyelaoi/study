import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main3 {

    /**
     *
     * 数据集
     * key : 账户ID，按自然序排序，先访问以小ID开始的转账记录
     *
     * value : 由键对应账户直接转出的账户ID集合
     *         转出集合按自然序排序
     *         深度优先遍历可以先遍历ID小的，再遍历ID大的账户
     *
     */
    static HashMap<Integer, TreeSet<Integer>> set = new HashMap<>();

    /**
     * 节点集
     * 只会记录出度大于零的节点
     */
    //static HashSet<Integer> nodeSet = new HashSet<>();
    //使用TreeSet,作为根节点顺序集，用于从小到大搜索
    static TreeSet<Integer> nodeSet = new TreeSet<>();

    /**
     * 结果集
     *
     */
    static List<Integer[]>[] result = new LinkedList[7];

    /**
     * 结果集2
     * 未进行排序
     * 需要一定的方式进行遍历，才能输出符合要求的顺序
     */
    static TreeMap<Integer, List<Integer[]>>[] tempResult = new TreeMap[7];
    static{
        for (int i = 0; i < tempResult.length; i++) {
            tempResult[i] = new TreeMap<>();
        }
    }


    /**
     * 使用此链表在递归中记录路径
     * 当满足循环转账条件时，将此链表中属于环的部分存储到结果集中
     * 由索引控制
     */
    static ArrayList<Integer> tempList = new ArrayList<>();

    /**
     * 记录活结点
     */
    static HashSet<Integer> vivSet = new HashSet<>();


    /**
     * 当前搜索的根节点
     * 每开始一个新的起始点，使用新的起始点初始化此属性
     */
    static int nowRoot;

    /**
     * 循环转账结果集总数
     */
    static int count = 0;

    /**
     * 用于集合返回整型数组的辅助参数参数
     */
    static final Integer[] CASE_FORMAT = new Integer[0];

    /**
     * 节点入度
     */
    static HashMap<Integer, Integer> inNum = new HashMap<>();

    /**
     * 节点出度
     */
    static HashMap<Integer, Integer> outNum = new HashMap<>();


    /**
     *
     * 完全深度遍历，从某根节点遍历到最深处，
     * 从深处向上回溯， 在该路径上只进行一次访问，当其他支路从新访问到活结点，可以仅进行深度为4的递归
     * @param nid
     * @param deep 深度控制，当访问到活结点，但该节点不在当前路径中，控制递归深度小于7
     * @param vivFlag 要访问的为已经访问过的根节点
     * @param vivRootIndex 活节点图跟节点的索引，如果环出现在活节点之后，则不算入环
     * @version v2.2 优化1.0
     *
     */
    private static void search(int nid, int deep, boolean vivFlag, int vivRootIndex){
        if((count+1)%1000 == 0){
            System.out.println(count);
        }
        int len = tempList.size();
        int idIndex = tempList.indexOf(nid);
        if (idIndex >= 0 ){ // 终止条件 1：当前节点在之前的路径中已经出现， 比较耗时
            /**
             * 访问到的路径中之前的节点
             * 判断是否为符合要求的路径
             */
            if(vivFlag && idIndex >= vivRootIndex) return; // 如果环在活图中，避免重复，直接return
            int l = len - idIndex; // 元素个数
            if(l < 8 && l >2){ // 当前路径小于7 说明环符合要求，因为不存在相互转账，所以路径一定大于等于2
                Integer[] integers = new Integer[l];
                for (int i = 0; i < l; i++) {
                    integers[i] = tempList.get(idIndex+i); // 取出路径
                }
                int minIndex = 0; // 最小值所在索引
                int min = nid;
                for (int i = 1; i < l; i++) {
                    if (min > integers[i]){
                        min = integers[i];
                        minIndex = i;
                    }
                }
                if (minIndex>0){// 调换顺序
                    int temp;
                    int tt;
                    for (int i = 0; i < l; i++) {
                        temp = integers[i];
                        tt = i < minIndex ? (l - (minIndex-i)) : (i - minIndex);
                        integers[i] = integers[tt];
                        integers[tt] = temp;
                    }
                }
                if(!tempResult[l-3].containsKey(nid)){
                    LinkedList<Integer[]> list = new LinkedList<>(); // 直接有序
                    tempResult[l-3].put(nid, list);
                }
                tempResult[l-3].get(nid).add(integers);
                count++;
            }
            return;
        }
        // 访问到路径中没有的节点
        Set<Integer> tempSet = set.get(nid);
        if (tempSet == null) return; // 终止条件2： 访问到的节点出度为0

        if (vivFlag && deep < 1) return; // 终止条件3： 访问到的为活结点，并且活结点深度到达了5
        tempList.add(nid);
        if(vivSet.contains(nid)){ // 访问到活结点
            /**
             * 访问活结点为根的图时，该图中的其他节点一定也都为活结点
             * 通过deep控制深度，保证在合理的情况下返回
             */
            if (!vivFlag){ // 该节点不是活结点的子节点
                deep = 4;
                vivFlag = true;
                vivRootIndex = len;
            }else{ // 该节点是活结点的子节点
                deep--;
            }
        }
        for (Integer nextId : tempSet){
            search(nextId, deep, vivFlag, vivRootIndex);
        }
        vivSet.add(nid);
        nodeSet.remove(nid); // 被访问过的活结点，从节点列表中删除，剩余没有访问过的节点
        tempList.remove(len);
    }


    public static void main(String[] args) throws InterruptedException {

        /**
         * 1. 数据读取， 并有序化
         */
        double start = System.nanoTime();
        loadFile(".\\test_data1.txt", false);
        double read = System.nanoTime();
        System.out.println("read time:" + (read - start)/1000/1000 + " MS");

        clear(); // 清理出度或入度为0的节点

        System.out.println(nodeSet);
        while(nodeSet.size()>0){
            Integer id = nodeSet.first();
            System.out.println("root id :  " + id);
            search(id,-1, false,0);
        }


        double search = System.nanoTime();
        System.out.println(count);

        System.out.println("count:" + count);
        System.out.println("read time:" + (read - start)/1000/1000 + " MS");
        System.out.println("search time:" + (search - read)/1000/1000 + "MS");

    }

    private static void print(){
        for (int i = 0; i < result.length; i++) {
            List<Integer[]> linkedLists = result[i];
            if (linkedLists != null && linkedLists.size()>0) {
                for (Integer[] ins : linkedLists) {
                    System.out.println(Arrays.toString(ins));
                }
            }
        }
    }

    /**
     * 删除出度为零，或入度为零的节点
     */
    private static void clear(){
        int size = nodeSet.size();
        long start = System.nanoTime();
        System.out.println("原size： " + size);
        boolean flag = true; // 有节点被清理
        while(flag){
            flag = false;
            // 每次遍历出点不为零的节点集合
            Iterator<Integer> iterator = nodeSet.iterator();
            while(iterator.hasNext()){
                Integer id = iterator.next();
                Integer num = inNum.get(id);
                if (num == null || num <= 0){ // 该节点入度为零
                    iterator.remove(); // 将该节点从起始点集合中删除
                    TreeSet<Integer> set1 = set.get(id);
                    for (Integer nid : set1){
                        Integer n = inNum.get(nid); // 被删除节点的下一个节点入度减一
                        inNum.put(nid, n-1);
                    }
                    set.remove(id);  // 将该节点的转账记录删除
                    flag = true;
                }
            }
        }
        int size2 = nodeSet.size();
        System.out.println("清理掉： " + (size2 - size));
        System.out.println("处理后size： " + size2);
        System.out.println("清理花费时间：" + (System.nanoTime()-start)/1000/1000 + " MS");
    }

    /**
     * 进行深度为2的搜索，并记录
     * 用于减少第七层的递归
     * 因为平均每个节点的出度为10，所以每个节点在两层之内平均可达100个其他节点
     * 能够极大的缩减递归次数
     * @param nid
     * @param l
     */
    private static void search2(int nid, int l){

    }



    /**
     * 读取文件
     * @param fileName
     * @param skipTitle
     * @return
     */
    public static void loadFile(String fileName, boolean skipTitle) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException exception) {
            System.err.println(fileName + " File Not Found");
            return;
        }
        String line = "";
        try {
            if (skipTitle) {
                reader.readLine();
            }
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                int a = Integer.valueOf(item[0]);
                int b = Integer.valueOf(item[1]);
                Integer n = inNum.get(b);
                if (n == null){
                    inNum.put(b, 1);
                }else{
                    inNum.put(b, n + 1);
                }
                if(set.containsKey(a)){ // 如果以a为起点的转账已经存在
                    set.get(a).add(b); // 将该转账记录直接加在a的转账集后面
                    outNum.put(a, outNum.get(a)+1);
                }else { // 新建一个以a，为起始的转账记录
                    TreeSet<Integer> newSet = new TreeSet<>();
                    newSet.add(b); // 将数据插入
                    set.put(a, newSet);
                    outNum.put(a, 1);
                    nodeSet.add(a);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

}
