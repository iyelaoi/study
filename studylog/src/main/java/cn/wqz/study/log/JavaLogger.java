package cn.wqz.study.log;

import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class JavaLogger {
    private static Logger logger = Logger.getLogger("cn.wqz.study.log");


    public static void main(String[] args) {
        logger.severe("严重");
        logger.warning("警告");
        logger.info("信息");
        logger.config("配置");
        logger.fine("良好");
        logger.finer("更好");
        logger.finest("最好");
        Properties properties = PropertiesFactory.initProperties("log.properties");
        if(properties != null){
            properties.list(System.out);
        }
    }
}
