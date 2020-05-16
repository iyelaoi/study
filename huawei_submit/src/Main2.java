import java.io.*;
import java.util.*;

/**
 * 2020-04-13 21:14:20	7.3392
 * 使用两层记忆处理dfs第六七层
 */
public class Main2 {
    static HashMap<Integer, TreeSet<Integer>> set = new HashMap<>(65536);
    static TreeSet<Integer> nodeSet = new TreeSet<>();
    static List<Integer[]>[] result = new LinkedList[5];
    static{
        for (int i = 0; i < 5; i++) {
            result[i] = new LinkedList<>();
        }
    }
    static List<Integer> tempList = new ArrayList<>();
    static HashMap<Integer, HashMap<Integer, LinkedList<Integer>>> twoLayer = new HashMap<>();
    static int nowRoot;
    static int count = 0;
    static final Integer[] CASE_FORMAT = new Integer[0];
    static HashMap<Integer, Integer> inNum = new HashMap<>();
    static final String LINE_SEPARATOR  = System.lineSeparator();

    private static void search(int nid, int l){
        if(l > 4) return;
        if(tempList.contains(nid)) return;
        Set<Integer> nidSet = set.get(nid);
        if (nidSet == null) return;
        tempList.add(nid);
        l++;
        HashMap<Integer,LinkedList<Integer>> hashMap = null;
        if(l == 5){ //
            hashMap = twoLayer.get(nid);
            if (hashMap != null){
                LinkedList<Integer> linkedList = hashMap.get(nowRoot);
                if(linkedList != null){
                    for(Integer midId : linkedList){
                        if (tempList.contains(midId)) continue;
                        if (midId < nowRoot) continue;
                        tempList.add(midId);
                        result[3].add(tempList.toArray(CASE_FORMAT));
                        count++;
                        tempList.remove(5);
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
                    hashMap = twoLayer.get(nextId);
                    if (hashMap != null){
                        LinkedList<Integer> linkedList = hashMap.get(nowRoot);
                        if(linkedList != null){
                            for(Integer midId : linkedList){
                                if (tempList.contains(midId)) continue;
                                if (midId < nowRoot) continue;
                                tempList.add(nextId);
                                tempList.add(midId);
                                result[4].add(tempList.toArray(CASE_FORMAT));
                                count++;
                                tempList.remove(6);
                                tempList.remove(5);
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
        tempList.remove(l-1);
    }

    static void solve(){
        Iterator<Integer> iterator = nodeSet.iterator();
        iterator.next();
        iterator.next();
        while(iterator.hasNext()){
            int rootId = iterator.next();
            for (int midId : set.get(rootId)){
                for (int endId: set.get(midId)) {
                    if (endId < rootId){
                        if(!twoLayer.containsKey(rootId)){
                            LinkedList<Integer> linkedList = new LinkedList<>();
                            linkedList.add(midId);
                            HashMap<Integer,LinkedList<Integer>> hashMap = new HashMap<>();
                            hashMap.put(endId, linkedList);
                            twoLayer.put(rootId, hashMap);
                        }else {
                            HashMap<Integer, LinkedList<Integer>> hashMap = twoLayer.get(rootId);
                            if (!hashMap.containsKey(endId)){
                                LinkedList<Integer> linkedList = new LinkedList<>();
                                linkedList.add(midId);
                                hashMap.put(endId, linkedList);
                            }else {
                                hashMap.get(endId).add(midId);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        loadFile("/data/test_data.txt");
        clear1();
        clear2();
        solve();
        for (Integer id : nodeSet){
            nowRoot = id;
            tempList.clear();
            search(id,0);
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
    }

    public static void loadFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException exception) {
            System.err.println(exception);
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
