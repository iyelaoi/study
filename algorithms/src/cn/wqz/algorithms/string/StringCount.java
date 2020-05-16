package cn.wqz.algorithms.string;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class StringCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> map = new HashMap<>();
        String s = null;
        int n = 0;
        while(scanner.hasNext()){
            s = scanner.next();
            if(map.containsKey(s)){
                n = map.get(s) + 1;
                map.replace(s, n);
            }else{
                map.put(s, 0);
            }
        }
    }
}
