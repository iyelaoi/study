package cn.wqz.crawler.zhihuuser.service;

import cn.wqz.crawler.zhihuuser.BloomFilter;
import cn.wqz.crawler.zhihuuser.ZhiHuUserBean;
import cn.wqz.crawler.zhihuuser.util.DBStatement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.wqz.crawler.zhihuuser.util.GetFromUrl.getFromUrl;

/**
 * 知乎用户
 */
public class SpiderService {
    //使用BloomFilter算法来去重
    static BloomFilter filter = new BloomFilter();

    //用户url阻塞队列
    static BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();//未爬过的网页url

    //数据库连接
    static Connection con = DBStatement.getCon();

    static PreparedStatement ps = null;

    static Executor executor = Executors.newFixedThreadPool(20);
    public static void main(String[] args) throws InterruptedException {
        //从我的主页开始爬
//        urlQueue.put("https://www.zhihu.com/people/yao-cheng-46");
        urlQueue.put("https://www.zhihu.com/people/yanwenya");
        System.out.println("初始队列大小:"+urlQueue.size());
        System.out.println("开始爬虫.........................................");
        for (int i=0;i<5;i++){
            Thread a = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String tmp = getAUrl();
                        if (!filter.contains(tmp)) {
                            filter.add(tmp);
                            //System.out.println(Thread.currentThread().getName()+"正在爬取url:"+tmp);
                            if (tmp != null) {
                                crawler(tmp);
                            }
                        }else {
                            System.out.println("此url存在，不爬了." + tmp);
                        }
                    }
                }
            });
            executor.execute(a);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        //    System.out.println("当前活动线程数："+((ThreadPoolExecutor)executor).getActiveCount());
                        if(((ThreadPoolExecutor)executor).getActiveCount()<5){
                            Thread a = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (true) {
                                        String tmp = getAUrl();
                                        if (!filter.contains(tmp)) {
                                            filter.add(tmp);
                                            //System.out.println(Thread.currentThread().getName()+"正在爬取url:"+tmp);
                                            if (tmp != null) {
                                                crawler(tmp);
                                            }
                                        }else {
                                            System.out.println("此url存在，不爬了." + tmp);
                                        }
                                    }
                                }
                            });
                            executor.execute(a);
                            if (urlQueue.size()==0){
                                System.out.println("队列为0了！！！！！！！！！！1");
                            }
                        }
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static String getAUrl() {
        String tmpAUrl;
        try {
            tmpAUrl= urlQueue.take();
            return tmpAUrl;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void crawler(String tmp) {

        Pattern pattern;
        Matcher matcher;
        //Jsoup解析网页
        Element userUrlContent = null;

        //解析网页
        userUrlContent = Jsoup.parse(getFromUrl(tmp));
        String userContent = userUrlContent.text();
        ZhiHuUserBean user = new ZhiHuUserBean();

//                System.out.println(userContent.substring(userContent.lastIndexOf("获得")+3,userContent.indexOf("次感谢")-1));

        //姓名
        String name = userUrlContent.select(".ProfileHeader-name").first().text();
        user.setName(name);
        //行业 公司 职位
        int size = userUrlContent.select(".ProfileHeader-infoItem").size();
        if (size == 2) {
            String string1 = userUrlContent.select(".ProfileHeader-infoItem").first().text();
            if (string1 != null && string1 != "") {
                String[] a = string1.split(" ");
                //行业
                for (int i = 0; i < a.length; i++) {
                    if (a.length > 0) {
                        user.setBusiness(a[0]);
                    }
                    //公司
                    if (a.length > 1) {
                        user.setCompany(a[1]);
                    }
                    //职位
                    if (a.length > 2) {
                        user.setPosition(a[2]);
                    }
                }
            }
            String string2 = userUrlContent.select(".ProfileHeader-infoItem").get(1).text();
            if (string2 != null && string2 != "") {
                String[] a = string2.split(" ");
                //学校
                if (a.length > 0) {
                    user.setEducation(a[0]);
                }
                //专业
                if (a.length > 1) {
                    user.setMajor(a[1]);
                }
            }
        }

        //看‘关注他’中有无关键字，判断性别
        String sexString = userUrlContent.select(".ProfileHeader-contentFooter button[icon]").first().text();
        if (sexString.contains("他")) {
            user.setSex(0);
        } else if (sexString.contains("她")) {
            user.setSex(1);
        } else {
            user.setSex(2);
        }

        // 回答数量
//                    String answersNum = userUrlContent.select(".ProfileMain-header").first().select("li").get(1).text().substring(2);
        user.setAnswersNum(userContent.substring(userContent.indexOf("回答")+2,userContent.indexOf("分享")-1));

        // 被赞同数
//                    Elements e1 = userUrlContent.select(".IconGraf");
//                    String starsNumString = e1.get(e1.size() - 2).text();
//                    String starsNum = starsNumString.substring(starsNumString.indexOf("得") + 2, starsNumString.indexOf("次") - 1);

        user.setStarsNum(userContent.substring(userContent.indexOf("获得")+3,userContent.indexOf("次赞同")-1));
        user.setThxNum(userContent.substring(userContent.lastIndexOf("获得")+3,userContent.indexOf("次感谢")-1));

        // 被感谢数
//                    Elements e2 = userUrlContent.select(".Profile-sideColumnItemValue");
//                    String thxNumString = e2.get(e2.size() - 1).text();
//                    String thxNum = thxNumString.substring(thxNumString.indexOf("得") + 2, thxNumString.indexOf("次") - 1);


        //关注的人
        String followingNum = userUrlContent.select(".NumberBoard-value").first().text();
        user.setFollowingNum(followingNum);

        //关注者数量
        String followersNum = userUrlContent.select(".NumberBoard-value").get(1).text();
        user.setFollowersNum(followersNum);


        //打印用户信息
        System.out.println("爬取成功：" + user);

        String sql = "insert into user " +
                "(name,sex,business,company,position,education,major,answersNum,starsNum,thxNum,followingNum,followersNum) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql, Statement.SUCCESS_NO_INFO);
            ps.setString(1, user.getName());
            ps.setInt(2, user.getSex());
            ps.setString(3, user.getBusiness());
            ps.setString(4, user.getCompany());
            ps.setString(5, user.getPosition());
            ps.setString(6, user.getEducation());
            ps.setString(7, user.getMajor());
            ps.setString(8, user.getAnswersNum());
            ps.setString(9, user.getStarsNum());
            ps.setString(10, user.getThxNum());
            ps.setString(11, user.getFollowingNum());
            ps.setString(12, user.getFollowersNum());
            //存储user
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        //把用户所关注的人的url加入到阻塞队列
        addUserFollowingUrl(tmp);

    }

    //传入用户url，获取他所关注人的url
    public static void addUserFollowingUrl(String userUrl){
        int i = 1;
        String userFollowingUrl = "";
        userFollowingUrl = userUrl+"/following?page=" + i;
        i++;
        Element userFollowingContent = null;
        userFollowingContent = Jsoup.parse(getFromUrl(userFollowingUrl));
        Elements followingElements = userFollowingContent.select(".List-item");
        //判断当前页关注人数是否为0，是的话就跳出循环
        if (followingElements.size() != 0) {
            for (Element e : followingElements) {
                String newUserUrl = e.select("a[href]").get(0).attr("href");
                //把获取到的地址加入阻塞队列
                try {
                    if (!newUserUrl.contains("org")) {
                        urlQueue.put("https://www.zhihu.com" + newUserUrl);
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    static class ErrHandler implements Thread.UncaughtExceptionHandler {
        /**
         * 这里可以做任何针对异常的处理,比如记录日志等等
         */
        public void uncaughtException(Thread a, Throwable e) {
            System.out.println("出现异常，重开一个线程");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String tmp = getAUrl();
                        if (!filter.contains(tmp)) {
                            filter.add(tmp);
                            //System.out.println(Thread.currentThread().getName()+"正在爬取url:"+tmp);
                            if (tmp != null) {
                                crawler(tmp);
                            }
                        }else {
                            System.out.println("此url存在，不爬了." + tmp);
                        }
                    }
                }
            });
        }
    }
}
