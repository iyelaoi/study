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
     * 结果集
     *
     */
    static List<Integer[]>[] result = new LinkedList[7];
    static int count = 0;

    /**
     * 搜索算法
     * 递归实现
     * @param vid 当前搜索的根节点ID
     * @param nid 当前访问的节点ID
     * @param l 当前路径深度
     * @param results 用于存储某条路径
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


    public static void main(String[] args) {

        /**
         * 1. 数据读取， 并有序化
         */
        double start = System.nanoTime();
        loadFile(".\\test_data1.txt", false);
        double read = System.nanoTime();
        System.out.println("read time:" + (read - start)/1000/1000 + " MS");

       /* // System.out.println(set);

        for (Integer id : set.keySet()){
            search(id,id, 1, new Integer[0]);
        }
        double search = System.nanoTime();
        System.out.println("search time:" + (search - read)/1000/1000 + "MS");

        System.out.println(count);
        for (int i = 0; i < result.length; i++) {
            List<Integer[]> linkedLists = result[i];
            if (linkedLists != null && linkedLists.size()>0) {
                for (Integer[] ins : linkedLists) {
                    System.out.println(Arrays.toString(ins));
                }
            }
        }*/
        System.out.println(count);
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
