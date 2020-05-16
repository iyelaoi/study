import java.io.*;
import java.util.*;

/**
 * 2020-04-12 17:28:04	14.8921
 * 首次代码提交
 * 纯七层递归，基本剪枝操作
 */
public class Main1 {
    static HashMap<Integer, TreeSet<Integer>> set = new HashMap<>(65536);

    static TreeSet<Integer> nodeSet = new TreeSet<>();

    static List<Integer[]>[] result = new LinkedList[7];

    static List<Integer> tempList = new ArrayList<>();

    static int nowRoot;

    static int count = 0;

    static final Integer[] CASE_FORMAT = new Integer[0];

    static HashMap<Integer, Integer> inNum = new HashMap<>();

    static final String LINE_SEPARATOR  = System.lineSeparator();

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

    private static void search(int nid, int l){
        if(l > 6) return;
        if(tempList.contains(nid)) return;
        Set<Integer> nidSet = set.get(nid);
        if (nidSet == null) return;
        tempList.add(nid);
        l++;
        for (int nextId : nidSet) {
            if (nextId < nowRoot) {
                continue;
            }else if (nextId > nowRoot){
                search(nextId, l);
            }else {
                if(l > 2){
                    if(result[l-3] == null) result[l-3] = new LinkedList<>();
                    result[l-3].add(tempList.toArray(CASE_FORMAT));
                    count++;
                }
            }
        }
        tempList.remove(l-1);
    }

    static void out(String name){
        BufferedWriter bufferedWriter = null;
        try{
            File file = new File(name);
            file.getParentFile().mkdirs();
            file.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.append("" + count);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                List<Integer[]> linkedLists = result[i];
                if (linkedLists != null && linkedLists.size()>0) {
                    for (Integer[] ids : linkedLists) {
                        stringBuilder.append(LINE_SEPARATOR);
                        stringBuilder.append(ids[0]);
                        for (int j = 1; j < ids.length; j++){
                            stringBuilder.append(","+ ids[j]);
                        }
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

    public static void main(String[] args) {
/*        loadFile("/data/test_data.txt");
        clear1();
        clear2();
        for (Integer id : nodeSet){
            nowRoot = id;
            tempList.clear();
            search(id,0);
        }
        out("/projects/student/result.txt");*/
        long s = System.nanoTime();
        loadFile("/data/test_data.txt");
        long r = System.nanoTime();
        clear1();
        clear2();
        for (Integer id : nodeSet){
            nowRoot = id;
            tempList.clear();
            search(id,0);
        }
        long sTime = System.nanoTime();
        out("/projects/student/result.txt");
        long pTime = System.nanoTime();
        System.out.println("read time: " + (r-s)/1000/1000 + "MS");
        System.out.println("search time: " + (sTime-r)/1000/1000 + " MS");
        System.out.println("print time: " + (pTime - sTime)/1000/1000 + " MS");
        System.out.println("total time: " + (System.nanoTime() - s)/1000/1000 + "MS");
    }

}
