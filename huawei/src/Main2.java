import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main2 {

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
    static TreeMap<Integer, TreeSet<Integer>> set = new TreeMap<>();

    /**
     * 节点集
     * 只会记录出度大于零的节点
     */
    static HashSet<Integer> nodeSet = new HashSet<>();

    /**
     * 结果集
     *
     */
    static List<Integer[]>[] result = new LinkedList[7];

    /**
     * 针对从每个节点开始的路径，使用此链表在递归中记录路径，每开始一个新的起始节点，清空此链表
     * 当满足循环转账条件时，将此链表所表示的路径存到循环转账结果集中
     */
    static List<Integer> tempList = new ArrayList<>();

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
     * 搜索算法
     * 递归实现
     * @param vid 当前搜索的根节点ID
     * @param nid 当前访问的节点ID
     * @param l 当前路径深度
     * @param results 用于存储某条路径
     * @version v2.0
     */
    private static void search(int vid, int nid, int l, Integer[] results){
        if (l > 7) return;
        for (int x : results) { // 如果访问到的节点在该路径中已经存在，则忽略该节点的搜索
            if (x == nid){return;}
        }
        if (!set.containsKey(nid)){
            return;
        }else{
            TreeSet<Integer> set1 = set.get(nid);
            if(set1.last()< vid) return;
            /**
             * 上面都属于终止条件
             * 下面进行逻辑处理
             */
            Integer[] r = new Integer[l]; // 用于存储从根节点到当前节点的路径
            System.arraycopy(results,0,r,0,l-1);
            r[l-1] = nid;
            Iterator<Integer> integerIterator = set1.iterator();
            while(integerIterator.hasNext()){
                int nextId = integerIterator.next();
                if(nextId < vid){
                    continue;
                }
                if(nextId == vid && l > 2){  // 符合条件的环

                    if (result[l-3] == null){ // 如果该路径长度的环集合不存在，则新建
                        result[l-3] = new LinkedList<>();
                    }
                    result[l-3].add(r); // 将路径添加至结果集
                    count++;
         /*           for (int x : r){ // 将可达点添加至集合
                        reachRootSet.add(x);
                    }*/
                }
                /*if(vivSet.contains(nextId)){ // 访问到已经访问的
                    if (reachRootSet.contains(nextId)){ // 已经访问的节点可达根节点
                        search2(vid, nextId,,l+1,r);
                    }
                }else{

                }*/
                search(vid,nextId,l+1, r);
            }

        }
    }

    /**
     *
     * @param nid
     * @param l 路径中的节点数，路径为空时，l=0；将节点加入路径后再进行加1
     * @version v2.1
     */
    private static void search(int nid, int l){
        if (count % 1000 == 0) System.out.println(count);
        // 如果当前路径节点数大于7，终止
        if(l > 6) return;
        // 如果当前节点已经存在于当前路径中，终止
        if(tempList.contains(nid)) return;
        // 获取当前节点的所有邻接点
        Set<Integer> nidSet = set.get(nid);
        // 如果当前节点没有邻接点，终止
        if (nidSet == null) return;
        // 将当前节点加入路径
        tempList.add(nid);
        l++;
        for (int nextId : nidSet) {
            // 如果下一个节点的ID小于起始跟ID，不对此节点进行搜索
            if (nextId < nowRoot) {
                continue;
            }else if (nextId > nowRoot){
                // 遍历下一个节点
                search(nextId, l);
            }else {
                // 符合条件的环，将其加入结果集
                if(l > 2){
                    if(result[l-3] == null) result[l-3] = new LinkedList<>();
                    result[l-3].add(tempList.toArray(CASE_FORMAT));
                    count++;
                }
            }
        }
        tempList.remove(l-1);
    }


    public static void main(String[] args) throws InterruptedException {

        /**
         * 1. 数据读取， 并有序化
         */
        double start = System.nanoTime();
        loadFile(".\\test_data1.txt", false);
        double read = System.nanoTime();
        System.out.println("read time:" + (read - start)/1000/1000 + " MS");
        System.out.println(nodeSet);
        clear();

       // System.out.println(set);

        test2();

        double search = System.nanoTime();
        Thread.sleep(1000000);
        print();
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

    private static void test2(){
        for (Integer id : set.keySet()){
            nowRoot = id;
            tempList.clear();
            search(id,0);
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
