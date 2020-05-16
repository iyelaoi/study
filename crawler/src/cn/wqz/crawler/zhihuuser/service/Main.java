package cn.wqz.crawler.zhihuuser.service;
import cn.wqz.crawler.zhihuuser.util.HttpUtils;
import cn.wqz.crawler.zhihuuser.util.JsoupUtils;
import org.jsoup.nodes.Document;

public class Main {
    public static void main(String[] args) {
        String url = "https://www.zhihu.com/people/yao-cheng-46/";// 初始解析网页地址

        while(true){
            // 设置代理ip
            HttpUtils.setProxyIp();
            Document document = JsoupUtils.getDocument(url);// 得到的document一定是正常 的document
            System.out.println(document);
        }
    }
}
