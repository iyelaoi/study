import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2020-04-18 21:47:44	6.9228
 * 8.4 -》 6.2
 * 按照一定的比例分配给多线程任务
 * 使用了MapperedByteBuffer， 效率没有什么提升
 */
public class Main5 implements Runnable{

    private static final int THREAD_COUNT = 4;
    static HashMap<Integer, TreeSet<Integer>> set = new HashMap<>(65536);
    static TreeSet<Integer> nodeSet = new TreeSet<>();
    static HashMap<Integer, TreeSet<Integer>> nodeSharding = new HashMap<>();
    static {
        for (int i = 0; i < THREAD_COUNT; i++) {
            nodeSharding.put(i, new TreeSet<>());
        }
    }
    List<Integer[]>[] result = new LinkedList[5];
    LinkedHashSet<Integer> tempList = new LinkedHashSet<>();
    static HashMap<Integer, HashMap<Integer, LinkedList<Integer>>> twoLayer = new HashMap<>(65536);
    int nowRoot;
    int count = 0;
    static AtomicInteger totalCount = new AtomicInteger();
    int threadId = 0;
    static CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
    static final Integer[] CASE_FORMAT = new Integer[0];
    static HashMap<Integer, Integer> inNum = new HashMap<>();
    static final String LINE_SEPARATOR  = System.lineSeparator();

    Main5(int threadId){
        this.threadId = threadId;
        for (int i = 0; i < 5; i++) {
            result[i] = new LinkedList<>();
        }
    }

    private void search(int nid, int l){
        if(l > 4) return;
        if(tempList.contains(nid)) return;
        Set<Integer> nidSet = set.get(nid);
        if (nidSet == null) return;
        tempList.add(nid);
        l++;
        HashMap<Integer,LinkedList<Integer>> TLHashMap = null;
        if(l == 5){
            TLHashMap = twoLayer.get(nid);
            if (TLHashMap != null){
                LinkedList<Integer> linkedList = TLHashMap.get(nowRoot);
                if(linkedList != null){
                    for(Integer midId : linkedList){
                        if (tempList.contains(midId)) continue;
                        tempList.add(midId);
                        result[3].add(tempList.toArray(CASE_FORMAT));
                        count++;
                        tempList.remove(midId);
                    }
                }
            }
        }
        for (int nextId : nidSet) {
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
                                tempList.remove(midId);
                                tempList.remove(nextId);
                            }
                        }
                    }
                }else{
                    search(nextId, l);
                }
            }else {
                if(l > 2){
                    result[l-3].add(tempList.toArray(CASE_FORMAT));
                    count++;
                }
            }
        }
        tempList.remove(nid);
    }

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
                    if (endId < rootId && endId < midId){
                        LinkedList<Integer> linkedList = hashMap.get(endId);
                        if (linkedList == null){
                            linkedList = new LinkedList<>();
                            hashMap.put(endId, linkedList);
                        }
                        linkedList.add(midId);
                    }
                }
            }
        }
    }

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
        for (int id : treeSet){
            nowRoot = id;
            tempList.clear();
            search(id, 0);
        }
        totalCount.addAndGet(count);
        countDownLatch.countDown();
    }

    public static void main(String[] args){
        loadFile("/data/test_data.txt");
        clear1();
        clear2();
        solve();
        sharding();
        Main5[] main5s = new Main5[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++){
            main5s[i] = new Main5(i);
            new Thread(main5s[i]).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out("/projects/student/result.txt", main5s);
    }

    private static void clear1(){
        boolean flag = true;
        while(flag){
            flag = false;
            Iterator<Integer> iterator = nodeSet.iterator();
            while(iterator.hasNext()){
                Integer id = iterator.next();
                Integer num = inNum.get(id);
                if (num == null || num <= 0){
                    iterator.remove();
                    TreeSet<Integer> set1 = set.get(id);
                    for (Integer nid : set1){
                        Integer n = inNum.get(nid);
                        inNum.put(nid, n-1);
                    }
                    set.remove(id);
                    flag = true;
                }
            }
        }
    }

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
            if (set1.size() <= 0){
                tempIterator.remove();
                nodeSet.remove(startId);
            }
        }
    }

    static void out(String name, Main5[] main5s){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("" + totalCount.get() + LINE_SEPARATOR);
        for (int i = 0; i < 5; i++) {
            for (Main5 main5 : main5s){
                List<Integer[]> linkedLists = main5.result[i];
                if (linkedLists != null && linkedLists.size()>0) {
                    for (Integer[] ids : linkedLists) {
                        stringBuilder.append(ids[0]);
                        for (int n = 1; n < ids.length; n++){
                            stringBuilder.append(",");
                            stringBuilder.append(ids[n]);
                        }
                        stringBuilder.append(LINE_SEPARATOR);
                    }
                }
            }
        }
        byte[] bytes = stringBuilder.toString().getBytes();
        int size = bytes.length;
        MappedByteBuffer mappedByteBuffer = null;
        try{
            File file = new File(name);
            file.createNewFile();
            mappedByteBuffer = new RandomAccessFile(file, "rw").getChannel().map(FileChannel.MapMode.READ_WRITE,0,size);
            mappedByteBuffer.put(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException exception) {
            System.err.println(fileName + " File Not Found");
            return;
        }
        String line = "";
        try {
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
                if(set.containsKey(a)){
                    set.get(a).add(b);
                }else {
                    TreeSet<Integer> newSet = new TreeSet<>();
                    newSet.add(b);
                    set.put(a, newSet);
                    nodeSet.add(a);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
