import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 记忆型，记两层，采用先两层的方式
 * 做数据结构上的优化
 *
 * 开启多线程
 * 线下提升三倍
 * 线上较慢
 *
 * 针对三百万数据集，分布问题越大的点，环越少
 * 对节点sharding方式很重要，
 * 如果按顺序平局分配给每个线程【1，2，3，4】【5，6，7，8】.。。会导致靠前的线程环较多，靠后的线程环较少
 * 如果按顺序分别分配【1，3，5，7，9】【2，4，6，8，10】。。。用什么数据结构存储最终结果很重要（基本很慢）
 *
 * 本类进行多线程优化，使靠前的线程拥有较少的节点，靠后的线程拥有较多的节点
 * 这样一来，每个线程维护自己的结果集，顺序的方式插入结果，顺序的方式读取结果，应该会很快
 *
 * 经过测试，速度并没有很大的提升
 *
 * 实现三层记录
 *
 * 算法正确，效率挺高
 *
 * 使用较好的sharding
 *
 */
public class Main implements Runnable{

    private static final int THREAD_COUNT = 4;

    /**
     * 数据集
     */
    static HashMap<Integer, TreeSet<Integer>> set = new HashMap<>(65536*2);

    /**
     * 节点集
     * 只会记录出度大于零的节点
     * 节点集和数据集共同完成数据记录
     * 节点集使用Treeset，保证遍历顺序
     * 数据集使用hash，保证搜索速度
     */
    static TreeSet<Integer> nodeSet = new TreeSet<>();

    static HashMap<Integer, TreeSet<Integer>> nodeSharding = new HashMap<>();
    static {
        for (int i = 0; i < THREAD_COUNT; i++) {
            nodeSharding.put(i, new TreeSet<>());
        }
    }

    /**
     * 结果集
     * 结果集使用链表数组的形式，每个索引位置分别记录不同长度的路径
     */
    List<Integer[]>[] result = new ArrayList[5];

    /**
     * 针对从每个节点开始的路径，使用此链表在递归中记录路径，每开始一个新的起始节点，清空此链表
     * 当满足循环转账条件时，将此链表所表示的路径存到循环转账结果集中
     * 记录顺序，又能快速查找
     */
    ArrayList<Integer> tempList = new ArrayList<>();

    /**
     * 用于记录两层的路径
     * HashMap<Integer（起始点）, HashMap<Integer（终止点）, LinkedList<Integer>>（中间点）>
     */
    static HashMap<Integer, HashMap<Integer, LinkedList<Integer>>> twoLayer = new HashMap<>(65536*2);


    /**
     * 三层记忆
     */
    static HashMap<Integer, HashMap<Integer, LinkedList<Integer[]>>> threeLayer = new HashMap<>(65536*2);


    /**
     * 用于两层节点暂存所到目的节点的所有中间点
     */


    /**
     * 当前搜索的根节点
     * 每开始一个新的起始点，使用新的起始点初始化此属性
     */
    Integer nowRoot;

    /**
     * 循环转账结果集总数
     */
    int count = 0;
    static AtomicInteger totalCount = new AtomicInteger();
    int threadId = 0;
    static CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
    Main(int threadId){
        this.threadId = threadId;
        for (int i = 0; i < 5; i++) {
            result[i] = new ArrayList<>();
        }
    }

    /**
     * 用于集合返回整型数组的辅助参数参数
     */
    static final Integer[] CASE_FORMAT = new Integer[0];

    /**
     * 节点入度
     */
    static HashMap<Integer, Integer> inNum = new HashMap<>(65536);


    static final String LINE_SEPARATOR  = System.lineSeparator();

    /**
     * 嵌套循环的方式实现
     *
     */
    void search(){
        tempList.add(nowRoot);
        for (Integer twoId : set.get(nowRoot)){
            if (twoId < nowRoot) continue;
            tempList.add(twoId);
            for (Integer threeId: set.get(twoId)){
                if (threeId < nowRoot || threeId.equals(nowRoot)) continue;
                tempList.add(threeId);
                // 第四层节点遍历
                Set<Integer> fourIds = set.get(threeId);
                for (Integer fourId: fourIds){
                    /**
                     * 判断第四层节点，是不是根节点
                     * 如果是根节点则加入结果集
                     * 如果不是跟节点，需要访问第五层
                     */
                    if (fourId < nowRoot) continue;
                    if (fourId.equals(nowRoot)){ // 如果第四层是根节点，直接加入结果集
                        result[0].add(tempList.toArray(CASE_FORMAT));
                        count++;
                        continue;
                    }
                    if (fourId.equals(twoId) || fourId.equals(threeId)){
                        continue;
                    }
                    /**
                     * 如果第四层不是根节点且大于根节点，并且在路径中没有出现过
                     * 需要将该节点加入路径，并访问下一层节点
                     */
                    tempList.add(fourId);
                    // 获取3的两层节点
                    // [root, two, three, four]
                    /**
                     * 实现第四层加两层
                     */
                    HashMap<Integer, LinkedList<Integer>> layer42 = twoLayer.get(fourId);
                    if(layer42 != null){
                        LinkedList<Integer> linkedList = layer42.get(nowRoot);
                        if(linkedList != null){
                            for (Integer midId: linkedList){
                                if (midId.equals(twoId) || midId.equals(threeId)){
                                    continue;
                                }
                                tempList.add(midId);
                                result[2].add(tempList.toArray(CASE_FORMAT));
                                count++;
                                tempList.remove(midId);
                            }
                        }
                    }/* 第四层节点加两层逻辑实现， 之后不需要判断第五层的下一层节点*/
                    for (Integer fiveId: set.get(fourId)){
                        /**
                         * 遍历第五层节点
                         * 第五层是根节点
                         * 加前四层到结果集
                         */
                        if (fiveId < nowRoot) continue;
                        if (fiveId.equals(nowRoot)){
                            /**
                             * 第五层是根节点
                             * 前四层路径需要被添加
                             */
                            result[1].add(tempList.toArray(CASE_FORMAT));
                            count++;
                            continue;
                        }
                        if (fiveId.equals(twoId) || fiveId.equals(threeId)){
                            /**
                             * 第五层节点与之前路径冲突
                             */
                            continue;
                        }
                        tempList.add(fiveId);
                        /**
                         * 添加第五层的两层记忆
                         */
                        HashMap<Integer, LinkedList<Integer>> layer52 = twoLayer.get(fiveId);
                        if(layer52 != null){
                            LinkedList<Integer> linkedList = layer52.get(nowRoot);
                            if (linkedList != null){
                                for (Integer mid: linkedList){
                                    if (mid.equals(twoId) || mid.equals(threeId) || mid.equals(fourId)){
                                        continue;
                                    }
                                    tempList.add(mid);
                                    result[3].add(tempList.toArray(CASE_FORMAT));
                                    count++;
                                    tempList.remove(5);
                                }
                            }
                        }
                        /**
                         * 添加第五层的三层记忆
                         */
                        HashMap<Integer, LinkedList<Integer[]>> layer53 = threeLayer.get(fiveId);
                        if (layer53 != null){
                            LinkedList<Integer[]> linkedList = layer53.get(nowRoot);
                            if(linkedList != null){
                                for (Integer[] tt : linkedList){
                                    int tt0 = tt[0];
                                    int tt1 = tt[1];
                                    if (tt0 == twoId || tt0 == threeId || tt0 == fourId ||
                                            tt1 == twoId || tt1 == threeId || tt1 == fourId){
                                        continue;
                                    }
                                    tempList.add(tt0);
                                    tempList.add(tt1);
                                    result[4].add(tempList.toArray(CASE_FORMAT));
                                    count++;
                                    tempList.remove(6);
                                    tempList.remove(5);
                                }
                            }
                        }/*第五层加三层记忆完成，整个序列完成*/
                        tempList.remove(4);
                    }
                    tempList.remove(3);
                }
                tempList.remove(2);
            }
            tempList.remove(1);
        }
        tempList.remove(0);
    }

    /**
     * 进行两层遍历，并记录
     * 优化代码细节
     */
    static void solve(){
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
            HashMap<Integer, LinkedList<Integer[]>> hashMap1 = threeLayer.get(rootId);
            if (hashMap1 == null){
                hashMap1 = new HashMap<>();
                threeLayer.put(rootId, hashMap1);
            }
            for (Integer midId : set.get(rootId)){
                for (Integer twoEndId: set.get(midId)) {
                    if (twoEndId < rootId && twoEndId < midId){ // 目的节点小于起始节点,并且目的节点小于中间节点
                        LinkedList<Integer> linkedList = hashMap.get(twoEndId);
                        if (linkedList == null){ // 没有终止节点相关的记录
                            linkedList = new LinkedList<>();
                            hashMap.put(twoEndId, linkedList);
                        }
                        linkedList.add(midId);
                    }
                    if(twoEndId.intValue() == rootId) continue;
                    for (Integer threeEndId: set.get(twoEndId)){
                        if(threeEndId < rootId && threeEndId < midId && threeEndId <twoEndId){
                            LinkedList<Integer[]> linkedList = hashMap1.get(threeEndId);
                            if (linkedList == null){
                                linkedList = new LinkedList<>();
                                hashMap1.put(threeEndId, linkedList);
                            }
                            linkedList.add(new Integer[]{midId, twoEndId});
                        }
                    }

                }
            }
        }
    }

    /**
     * 使用Main4的sharding
     */
    private static void sharding(){
        int nodeSize = nodeSet.size();
        int s1 = nodeSize/20;
        int s2 = nodeSize/10 + s1;
        int s3 = nodeSize/5;
        int i = 0;
        TreeSet<Integer> treeSet = nodeSharding.get(0);
        TreeSet<Integer> treeSet1 = nodeSharding.get(1);
        TreeSet<Integer> treeSet2 = nodeSharding.get(2);
        TreeSet<Integer> treeSet3 = nodeSharding.get(3);
        for (Integer id : nodeSet){
            i++;
            if (i > s3){
                treeSet3.add(id);
            }else if (i > s2){
                treeSet2.add(id);
            }else if (i > s1){
                treeSet1.add(id);
            }else {
                treeSet.add(id);
            }
        }
    }

    @Override
    public void run() {
        TreeSet<Integer> treeSet = nodeSharding.get(threadId);
        for (Integer id : treeSet){
            nowRoot = id;
            tempList.clear();
            search();
        }
        totalCount.addAndGet(count);
        countDownLatch.countDown();
    }

    public static void main(String[] args) {
        if (args.length == 0){
            try {
                Runtime.getRuntime().exec("java Main -Xmx4G -Xms4G");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            launch();
        }
    }

    static void launch(){
        loadFile("/data/test_data.txt", false);
        clear1();
        clear2();
        solve();
        sharding();
        Main[] mains = new Main[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++){
            mains[i] = new Main(i);
            new Thread(mains[i]).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        set.clear();
        twoLayer.clear();
        threeLayer.clear();
        nodeSet.clear();
        out("/projects/student/result.txt", mains);
    }

    /**
     * 删除入度为零的节点
     */
    private static void clear1(){
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
    }

    /**
     * 删除出度为零的节点
     * 出度清理方法错误
     */
    private static void clear2(){
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
                }
            }
            if (set1.size() <= 0){ // 如果该起始点的所有转出点都被删除，说明该结点已经没有意义
                tempIterator.remove();
                nodeSet.remove(startId);
            }
        }
    }

    static void out(String name, Main[] mains){
        BufferedWriter bufferedWriter = null;
        try{
            File file = new File(name);
            file.getParentFile().mkdirs();
            file.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.append("" + totalCount.get() + LINE_SEPARATOR);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                for (Main main : mains){
                    List<Integer[]> linkedLists = main.result[i];
                    if (linkedLists != null && linkedLists.size()>0) {
                        for (Integer[] ids : linkedLists) {
                            stringBuilder.append(ids[0]);
                            for (int n = 1; n < ids.length; n++){
                                stringBuilder.append(","+ ids[n]);
                            }
                            stringBuilder.append(LINE_SEPARATOR);
                            bufferedWriter.append(stringBuilder);
                            stringBuilder.delete(0,stringBuilder.length());
                        }
                    }
                }
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                Integer a = Integer.valueOf(item[0]);
                Integer b = Integer.valueOf(item[1]);
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
