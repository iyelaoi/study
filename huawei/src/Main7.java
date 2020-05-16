import java.io.*;
import java.util.*;

/**
 * 记忆型，记两层，采用先两层的方式
 * main6 做数据结构上的优化
 *
 * 批量使用两层记忆
 *
 * 优化失败, 效率很慢
 *
 */
public class Main7 {

    /**
     *
     * 数据集
     *
     */
    static HashMap<Integer, TreeSet<Integer>> set = new HashMap<>(65536);

    /**
     * 节点集
     * 只会记录出度大于零的节点
     * 节点集和数据集共同完成数据记录
     * 节点集使用Treeset，保证遍历顺序
     * 数据集使用hash，保证搜索速度
     */
    static TreeSet<Integer> nodeSet = new TreeSet<>();

    /**
     * 结果集
     * 结果集使用链表数组的形式，每个索引位置分别记录不同长度的路径
     */
    static List<Integer[]>[] result = new LinkedList[5];
    static{
        for (int i = 0; i < 5; i++) {
            result[i] = new LinkedList<>();
        }
    }

    /**
     * 针对从每个节点开始的路径，使用此链表在递归中记录路径，每开始一个新的起始节点，清空此链表
     * 当满足循环转账条件时，将此链表所表示的路径存到循环转账结果集中
     * 记录顺序，又能快速查找
     */
    static LinkedHashSet<Integer> tempList = new LinkedHashSet<>();

    /**
     * 用于记录两层的路径
     * HashMap<Integer（起始点）, HashMap<Integer（终止点）, LinkedList<Integer>>（中间点）>
     */
    static HashMap<Integer, HashMap<Integer, LinkedList<Integer>>> twoLayer = new HashMap<>(65536);

    /**
     * 用于两层节点暂存所到目的节点的所有中间点
     */


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


    static final String LINE_SEPARATOR  = System.lineSeparator();

    /**
     * 节点出度
     */
    //static HashMap<Integer, Integer> outNum = new HashMap<>();

    /**
     *
     * 原：按照原始dfs,更新至使用两层记录的记忆性递归
     * 现在批量使用两层记忆
     *
     * @param nid
     * @param l 路径中的节点数，路径为空时，l=0；将节点加入路径后再进行加1
     * @version v2.1
     */
    private static void search(int nid, int l){ // 进来就合理
        tempList.add(nid);
        l++;
        HashMap<Integer,LinkedList<Integer>> TLHashMap = twoLayer.get(nid);
        if(TLHashMap != null){
            LinkedList<Integer> linkedList = TLHashMap.get(nowRoot);
            if (linkedList != null){ // 使用记忆直接加
                for (int midId : linkedList) {
                    if (tempList.contains(midId)) continue;
                    tempList.add(midId);
                    result[l - 2].add(tempList.toArray(CASE_FORMAT));
                    count++;
                    tempList.remove(midId); // 加完就删
                }
            }
        }

        for (int nextId : set.get(nid)){
            if (nextId <= nowRoot){
                continue;
            }else {
                if (tempList.contains(nextId)) continue;
                if (l < 6 ) search(nextId,l);
            }
        }
        tempList.remove(nid);
    }

    /**
     * 进行两层遍历，并记录
     * 优化代码细节
     */
    static void solve(){
        long s = System.nanoTime();
        Iterator<Integer> iterator = nodeSet.iterator();
        iterator.next();
        iterator.next();
        while(iterator.hasNext()){
            int rootId = iterator.next();
            HashMap<Integer,LinkedList<Integer>> hashMap = twoLayer.get(rootId);
            if(hashMap == null){
                hashMap = new HashMap<>();
                twoLayer.put(rootId, hashMap);
            }
            for (int midId : set.get(rootId)){
                for (int endId: set.get(midId)) {
                    if (endId < rootId && endId < midId){ // 目的节点小于起始节点,并且目的节点小于中间节点
                        LinkedList<Integer> linkedList = hashMap.get(endId);
                        if (linkedList == null){ // 没有终止节点相关的记录
                            linkedList = new LinkedList<>();
                            hashMap.put(endId, linkedList);
                        }
                        linkedList.add(midId);
                    }
                }
            }
        }
        System.out.println("twolay time: " + (System.nanoTime()-s)/1000/1000 + " MS");
    }


    public static void main(String[] args){
        long s = System.nanoTime();
        loadFile("/data/test_data.txt", false);
        long r = System.nanoTime();
        clear1();
        clear2();
        solve();
        for (Integer id : nodeSet){
            nowRoot = id;
            tempList.add(nowRoot);
            for (int nextId : set.get(id)){
                if(nextId > nowRoot){
                    search(nextId, 1);
                }
            }
            tempList.clear();
        }
        long sTime = System.nanoTime();
        out("/projects/student/result.txt");
        long pTime = System.nanoTime();
        System.out.println("count: " + count);
        System.out.println("read time: " + (r-s)/1000/1000 + "MS");
        System.out.println("search time: " + (sTime-r)/1000/1000 + " MS");
        System.out.println("print time: " + (pTime - sTime)/1000/1000 + " MS");
        System.out.println("total time: " + (System.nanoTime() - s)/1000/1000 + "MS");
    }



    /**
     * 删除入度为零的节点
     */
    private static void clear1(){
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
     * 删除出度为零的节点
     */
    private static void clear2(){
        long start = System.nanoTime();
        int size = nodeSet.size();
        int temp = 0;
        Set<Map.Entry<Integer, TreeSet<Integer>>> tempSet = set.entrySet();
        Iterator<Map.Entry<Integer, TreeSet<Integer>>> tempIterator = tempSet.iterator();
        while (tempIterator.hasNext()) {
            Map.Entry<Integer, TreeSet<Integer>> entry = tempIterator.next();
            Integer startId = entry.getKey();
            TreeSet<Integer> set1 = entry.getValue();
            Iterator<Integer> iterator = set1.iterator();
            while(iterator.hasNext()){
                int id = iterator.next();
                if(!set.containsKey(id)){
                    iterator.remove();
                    temp++;
                }
            }
            if (set1.size() <= 0){ // 如果该起始点的所有转出点都被删除，说明该结点已经没有意义
                tempIterator.remove();
                nodeSet.remove(startId);
            }
        }
        int size2 = nodeSet.size();
        int size3 = size2 - size;

        System.out.println("出度清理掉起始点： " + size3);
        System.out.println("出度处理后起始点size： " + size2);
        System.out.println("出度处理掉的节点数" + (temp + size3));
        System.out.println("出度清理花费时间：" + (System.nanoTime()-start)/1000/1000 + " MS");

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

    static void out(String name){
        long s = System.nanoTime();
        BufferedWriter bufferedWriter = null;
        try{
            File file = new File(name);
            file.getParentFile().mkdirs();
            file.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.append("" + count + LINE_SEPARATOR);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                List<Integer[]> linkedLists = result[i];
                if (linkedLists != null && linkedLists.size()>0) {
                    for (Integer[] ids : linkedLists) {
                        stringBuilder.append(ids[0]);
                        for (int j = 1; j < ids.length; j++){
                            stringBuilder.append(","+ ids[j]);
                        }
                        stringBuilder.append(LINE_SEPARATOR);
                        bufferedWriter.append(stringBuilder);
                        stringBuilder.delete(0,stringBuilder.length());
                    }
                }
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("print time: " + (System.nanoTime() - s)/1000/1000 + "MS");
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
                    //outNum.put(a, outNum.get(a)+1);
                }else { // 新建一个以a，为起始的转账记录
                    TreeSet<Integer> newSet = new TreeSet<>();
                    newSet.add(b); // 将数据插入
                    set.put(a, newSet);
                    //outNum.put(a, 1);
                    nodeSet.add(a);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

}
