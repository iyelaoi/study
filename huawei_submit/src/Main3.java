import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 2020-04-15 22:38:07	8.4275
 *
 * 多线程版本
 * 线下提升三倍
 *
 * 线上较慢
 * 均匀分布【1，5】【2，6】【3，7】【4，8】
 * 使用共享结果集，导致性能损失
 */
public class Main3 implements Runnable{
    private static final int THREAD_COUNT = 4;
    static HashMap<Integer, TreeSet<Integer>> set = new HashMap<>(65536);
    static TreeSet<Integer> nodeSet = new TreeSet<>();
    static HashMap<Integer, TreeSet<Integer>> nodeSharding = new HashMap<>();
    static {
        for (int i = 0; i < THREAD_COUNT; i++) {
            nodeSharding.put(i, new TreeSet<>());
        }
    }
    static SortedMap<Integer, List<Integer[]>>[] tempResult = new SortedMap[5];
    static{
        for (int i = 0; i < tempResult.length; i++) {
            tempResult[i] = Collections.synchronizedSortedMap(new TreeMap());
        }
    }
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

    Main3(int threadId){
        this.threadId = threadId;
    }

    private void search(int nid, int l){
        if(l > 4) return;
        if(tempList.contains(nid)) return;
        Set<Integer> nidSet = set.get(nid);
        if (nidSet == null) return;
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
                        List<Integer[]> list = tempResult[3].get(nowRoot);
                        if(list == null) {
                            list = new LinkedList<>();
                            tempResult[3].put(nowRoot, list);
                        }
                        list.add(tempList.toArray(CASE_FORMAT));
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
                                List<Integer[]> list = tempResult[4].get(nowRoot);
                                if(list == null) {
                                    list = new LinkedList<>();
                                    tempResult[4].put(nowRoot, list);
                                }
                                list.add(tempList.toArray(CASE_FORMAT));
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
                    List<Integer[]> list = tempResult[l - 3].get(nowRoot);
                    if(list == null) {
                        list = new LinkedList<>();
                        tempResult[l - 3].put(nowRoot, list);
                    }
                    list.add(tempList.toArray(CASE_FORMAT));
                    count++;
                }
            }
        }
        tempList.remove(nid);
    }

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
        int i = 0;
        int temp = 0;
        TreeSet<Integer> treeSet = nodeSharding.get(temp);
        for (Integer id : nodeSet){
            treeSet.add(id);
            i++;
            treeSet = nodeSharding.get(i%THREAD_COUNT);
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
        loadFile("/data/test_data.txt", false);
        clear1();
        clear2();
        solve();
        sharding();
        Main3[] main8s = new Main3[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++){
            main8s[i] = new Main3(i);
            new Thread(main8s[i]).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        out("/projects/student/result.txt");
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

    static void out(String name){
        BufferedWriter bufferedWriter = null;
        try{
            File file = new File(name);
            file.getParentFile().mkdirs();
            file.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.append("" + totalCount.get() + LINE_SEPARATOR);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                for(Map.Entry<Integer, List<Integer[]>> entry : tempResult[i].entrySet()){
                    List<Integer[]> linkedLists = entry.getValue();
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
