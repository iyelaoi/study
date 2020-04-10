import sun.reflect.generics.tree.Tree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * 一个节点
 */
class N implements Comparable<N>{

    // 当前节点ID
    int id;

    boolean visited = false;

    // 当前节点所转出账户的节点有序列表，搜索时直接有序
    TreeSet<N> ns = new TreeSet<>();

    @Override
    public boolean equals(Object obj) {
        return (((N)obj).id == id);
    }

    @Override
    public int compareTo(N o) {
        return id - o.id;
    }
}

public class Main {

    /**
     * 数据集
     */
    static TreeMap<Integer, TreeSet<Integer>> set = new TreeMap<>();
    static List<Integer[]>[] result = new LinkedList[7];
    static Set<Integer> vivSet = new HashSet<>();
    static Set<Integer> reachRootSet = new HashSet<>(); // 每一个新的根节点，创建一个可达集合，集合中的每个元素都能通过某些路径到达根节点
    static int count = 0;

    /*static{
        int[][] e={
                {0,1,0,0,0,1,0},
                {0,0,1,0,0,0,0},
                {1,0,0,1,1,0,0},
                {1,0,0,0,1,1,0},
                {0,0,0,0,0,1,1},
                {0,0,1,0,0,0,1},
                {1,0,0,1,0,0,0}};
        for (int i = 0; i < 7; i++){
            TreeSet<Integer> integers = new TreeSet<>();
            for (int j = 0; j < 7; j++) {
                if(e[i][j] == 1){
                    integers.add(j+1);
                }
            }
            set.put(i+1, integers);
        }
    }*/




    /**
     * 每一个新的节点，创建一个可达map，每个键对应的值为该节点到达根节点的非重复集合
     * 操作时还需注意，不能随便加长度
     */
    static HashMap<Integer, List<int[]>[]> rmap = new HashMap<>(); //



    public static void main(String[] args) {

        /**
         * 1. 数据读取， 并有序化
         */
        double start = System.nanoTime();
        loadFile(".\\test_data.txt", false);
        double end = System.nanoTime();
        System.out.println((end - start)/1000/1000 + " MS");

        // System.out.println(set);

        for (Integer id : set.keySet()){
            search2(id,id, 1, new Integer[0]);
        }

        System.out.println(count);
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
     * 搜索算法
     * 非递归实现
     * 两个大问题：
     * 1. 路径的存储
     * 2. 环导致死循环问题
     */
    static void search(){
        Iterator<Integer> iterator = set.keySet().iterator();
        while(iterator.hasNext()){ // 按照从小到大的顺序访问节点
            Stack<Integer> stack = new Stack<>(); // 辅助深度优先搜索
            int vId = iterator.next(); // 拿到当前访问节点的ID
            System.out.println(iterator.next());
            stack.push(vId); // 当前节点入栈

            List<Integer> list = new ArrayList<>(); // 用于存储搜索路径,此处用ArrayList更快

            /**
             * 用于记录某节点插入路径时的栈深度
             * 当回溯到上一层时删除路径中的当前节点
             */
            HashMap<Integer, Integer> hashMap = new HashMap<>();
            while(list.size() < 8  && stack.size() > 0){

                // 访问第一个节点
                int nowId = stack.pop();
                hashMap.put(nowId, stack.size());


                // 记录栈深度，用于删除节点

                // 将节点加入将要搜索的路径结果链表
                /**
                 * 此处为算法重点，将直接影响算法的正确性
                 *
                 * 什么条件下将访问到的元素加入路径
                 *      加入路径后有怎样的处理逻辑，怎样处理这个链表
                 *
                 * 什么条件下不将访问到的元素加入路径
                 *      不加入后有怎样的处理逻辑，怎样处理这个链表
                 *
                 */
                list.add(nowId); // 不管怎样都加
                // 获取当前id所有的转账记录
                TreeSet set2 = set.get(nowId);
                /**
                 * 此处可能有没有必要的判断
                 * 比如说有这个节点，就一定有转账记录
                 */
                if(set2 != null && set2.size()>0){ // 当前节点有转出记录
                    // 因为id是按照从小到大顺序存储在集合中，若要从小到大遍历，必须从大到小进行压栈操作
                    Integer[] ints = (Integer[])set2.toArray(new Integer[1]);
                    boolean flag = false; // 用于标记有没有环
                    for(int i = ints.length-1; i-- >=0; i--){ // 逆序压栈
                        /**
                         * 此处可考虑两个问题：
                         * 1. 在数据集中用从大到小的顺序存储每个节点的转账记录
                         * 2. 没有转出记录的不进行压栈，或者在前面将没有转出记录的转账记录删除
                         * 第二种方式可能好些
                         */
                        // if(ints[i] > nowId){ // 1 5 3 1
                        if(ints[i] > vId){ // 只压id大于vId的元素
                            stack.push(ints[i]);
                        }else if (ints[i] == vId){ // 如果转出账户与vId相同，说明存在环
                            int len = list.size();
                            /**
                             * 算法上，已经先将目标环存储
                             */
                            if(len >2){ // 环尺寸大于2，此环符合搜索要求
                                // 将路径存于结果集，由于是从小到大遍历，存储结果直接有序
                                result[len - 3].add(list.toArray(new Integer[1]));

                            }
                        }
                    }
                }
                list.remove(nowId); // 遍历完成，删除当前节点
            }


        }
    }


    /**
     * 搜索算法
     * 递归实现
     * @param vid 当前搜索的根节点ID
     * @param nid 当前访问的节点ID
     * @param l 当前路径深度
     * @param results 用于存储某条路径
     */
    private static void search2(int vid, int nid, int l, Integer[] results){
        if (l > 7) return;
        for (int x : results) {
            if (x == nid){return;}
        }
        if (!set.containsKey(nid)){
            return;
        }else{
            TreeSet<Integer> set1 = set.get(nid);
            if(set1.last()< vid) return;
            vivSet.add(nid);
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
                if(nextId == vid && l > 2){
                    if (result[l-3] == null){
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
                search2(vid,nextId,l+1, r);
            }

        }

    }


    /**
     * 小范围递归
     * @param vid 根节点
     * @param nid 访问到的节点
     * @param rid 访问到的节点下出现的一个活结点
     * @param l
     * @param results
     */
    private static void search3(int vid, int nid, int rid, int l, int[] results) {

    }

    /**
     * 不合理： 难以去重
     * 解决方案， 小范围，单独dfs
     *
     * id是一个已经访问过且可以由某些路径到达当前根节点的节点
     * 由于遍历其他节点时访问到该节点，但是不能将其再次放入递归，避免死循环
     * 此方法用于返回该节点到根节点的所有路径，可根据之前的节点直接添加，构造环
     *
     * 此处还可提供记忆功能： 暂且不考虑
     * @param id
     * @return
     */
    private static LinkedList<int[]> append(int id){

        return null;

    }

    private static void readData(){

    }

    /**
     * 删除没有金钱转入的账户
     * @param data
     */
    private static void delNoIn(int[][] data){

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

                if(set.containsKey(a)){ // 如果以a为起点的转账已经存在
                    set.get(a).add(b); // 将该转账记录直接加在a的转账集后面
                }else { // 新建一个以a，为起始的转账记录
                    TreeSet<Integer> newSet = new TreeSet<>();
                    newSet.add(b); // 将数据插入
                    set.put(a, newSet);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

}
