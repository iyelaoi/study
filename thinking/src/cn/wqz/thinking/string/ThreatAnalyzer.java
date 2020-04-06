package cn.wqz.thinking.string;

import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.MatchResult;

public class ThreatAnalyzer {
    public static void main(String[] args) {
        String s = "...............";
        Scanner scanner = new Scanner(s);
        String regex = "\\s....";
        while(scanner.hasNext(regex)){
            scanner.next(regex);
            MatchResult matchResult = scanner.match();
            String s1 = matchResult.group(1);
            String s2 = matchResult.group(2);
        }
    }
}
