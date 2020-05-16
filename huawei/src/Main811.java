import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
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
 *
 * 经过测试，速度并没有很大的提升
 *
 */
public class Main811 implements Runnable{

    private static final int THREAD_COUNT = 4;

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
    List<Integer[]>[] result = new LinkedList[5];

    /**
     * 针对从每个节点开始的路径，使用此链表在递归中记录路径，每开始一个新的起始节点，清空此链表
     * 当满足循环转账条件时，将此链表所表示的路径存到循环转账结果集中
     * 记录顺序，又能快速查找
     */
    LinkedHashSet<Integer> tempList = new LinkedHashSet<>();

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
    int nowRoot;

    /**
     * 循环转账结果集总数
     */
    int count = 0;

    static AtomicInteger totalCount = new AtomicInteger();

    int threadId = 0;

    static CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

    Main811(int threadId){
        this.threadId = threadId;
        for (int i = 0; i < 5; i++) {
            result[i] = new LinkedList<>();
        }
    }

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
     * 按照原始dfs,更新至使用两层记录的记忆性递归
     * @param nid
     * @param l 路径中的节点数，路径为空时，l=0；将节点加入路径后再进行加1
     * @version v2.1
     */
    private void search(int nid, int l){
        // 如果当前路径节点数大于3，终止
        if(l > 4) return;
        // 如果当前节点已经存在于当前路径中，终止
        if(tempList.contains(nid)) return;
        // 获取当前节点的所有邻接点
        Set<Integer> nidSet = set.get(nid);
        // 如果当前节点没有邻接点，终止
        if (nidSet == null) return;
        // 将当前节点加入路径
        tempList.add(nid);
        l++;
        HashMap<Integer,LinkedList<Integer>> TLHashMap = null;
        if(l == 5){ //
            TLHashMap = twoLayer.get(nid);
            if (TLHashMap != null){
                LinkedList<Integer> linkedList = TLHashMap.get(nowRoot);
                if(linkedList != null){
                    for(Integer midId : linkedList){
                        if (tempList.contains(midId)) continue;
                        tempList.add(midId);
                        result[3].add(tempList.toArray(CASE_FORMAT));
                        count++;
                        tempList.remove(midId); // 加完就删
                    }
                }
            }
        }
        for (int nextId : nidSet) {
            // 如果下一个节点的ID小于起始跟ID，不对此节点进行搜索
            if (nextId < nowRoot) {
                continue;
            }else if (nextId > nowRoot){
                if (tempList.contains(nextId)) continue;
                if (l == 5){
                    TLHashMap = twoLayer.get(nextId);
                    if (TLHashMap != null){
                        LinkedList<Integer> linkedList = TLHashMap.get(nowRoot);
                        if(linkedList != null){
                            for(Integer midId : linkedList){
                                if (tempList.contains(midId)) continue;
                                tempList.add(nextId);
                                tempList.add(midId);
                                result[4].add(tempList.toArray(CASE_FORMAT));
                                count++;
                                tempList.remove(midId); // 加完就删
                                tempList.remove(nextId); // 加完就删
                            }
                        }
                    }
                }else{
                    // 遍历下一个节点
                    search(nextId, l);
                }
            }else {
                // 符合条件的环，将其加入结果集
                if(l > 2){
                    result[l-3].add(tempList.toArray(CASE_FORMAT));
                    count++;
                }
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

    private static void sharding(){
        int nodeSize = nodeSet.size();
        int i = 0;
        TreeSet<Integer> treeSet = nodeSharding.get(0);
        TreeSet<Integer> treeSet1 = nodeSharding.get(1);
        TreeSet<Integer> treeSet2 = nodeSharding.get(2);
        TreeSet<Integer> treeSet3 = nodeSharding.get(3);
        for (Integer id : nodeSet){
            i++;
            if (i >5000){
                treeSet3.add(id);
            }else if (i > 3000){
                treeSet2.add(id);
            }else if (i > 1000){
                treeSet1.add(id);
            }else {
                treeSet.add(id);
            }
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start");
        TreeSet<Integer> treeSet = nodeSharding.get(threadId);
        for (int id : treeSet){
            nowRoot = id;
            tempList.clear();
            search(id, 0);
        }
        totalCount.addAndGet(count);
        countDownLatch.countDown();
        System.out.println(Thread.currentThread().getName() + " end, set:  " + treeSet.size() + "result count: " + count);
    }

    public static void main(String[] args){

        loadFile1("/data/test_data.txt");
        clear1();
        clear2();
        solve();
        sharding();
        long s = System.nanoTime();
        Main811[] main811s = new Main811[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++){
            main811s[i] = new Main811(i);
            new Thread(main811s[i]).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long sTime = System.nanoTime();
        out("/projects/student/result.txt", main811s);
        long pTime = System.nanoTime();
        System.out.println("count: " + totalCount);
        System.out.println("search time: " + (sTime-s)/1000/1000 + " MS");
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

    static void out(String name, Main811[] main811s){
        long s = System.nanoTime();
        BufferedWriter bufferedWriter = null;
        try{
            File file = new File(name);
            file.getParentFile().mkdirs();
            file.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.append("" + totalCount.get() + LINE_SEPARATOR);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                for (Main811 main811 : main811s){
                    List<Integer[]> linkedLists = main811.result[i];
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
        System.out.println("print time: " + (System.nanoTime() - s)/1000/1000 + "MS");
    }

    public static void loadFile1(String fileName) {
        long s = System.nanoTime();
        try {
            FileChannel fc = new RandomAccessFile(new File(fileName), "r").getChannel();
            MappedByteBuffer mappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));


            int index = 0;
            Charset charset = Charset.forName("UTF-8");
            CharBuffer charBuffer = charset.newDecoder().decode(mappedByteBuffer);
            String data = charBuffer.toString();
            String[] s1 = data.split("\n");
            for (String s2 : s1) {
                String[] s3 = s2.split(",");
                int a = Integer.valueOf(s3[0]);
                int b = Integer.valueOf(s3[1]);
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
            /*int limit = mappedByteBuffer.limit();
            while(index < limit) {
                mappedByteBuffer.position(index);
                char cc = mappedByteBuffer.getChar();
                cc = mappedByteBuffer.getChar();
                int a = mappedByteBuffer.getInt();
                index += 5;
                mappedByteBuffer.position(index);
                int b = mappedByteBuffer.getInt();
                index += 10 ;
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
            }*/
            System.out.println("read time: " + (System.nanoTime() - s)/1000/1000 + " MS");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
