package cn.wqz.crawler;

import sun.nio.cs.ext.MacHebrew;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * 模拟基本的爬虫
 * 从目标网址对应的资源中获取其中所有的URL链接
 *
 */
public class Crawler {

    public static void main(String[] args) {
        URL url = null;
        URLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        String regex = "https://[\\w+\\.?/?]+\\.[A-Za-z]+";
        Pattern pattern = Pattern.compile(regex);
        try {
            url = new URL("https://www.rndsystems.com/cn");
            urlConnection = url.openConnection();
            printWriter = new PrintWriter(new FileWriter("./SiteUrl.txt"), true);
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String buf = null;
            while((buf = bufferedReader.readLine()) != null){
                Matcher matcher = pattern.matcher(buf);
                while(matcher.find()){
                    printWriter.println(matcher.group());
                }
            }
            System.out.println("爬取成功");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            printWriter.close();
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
