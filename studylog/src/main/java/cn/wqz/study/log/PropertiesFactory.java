package cn.wqz.study.log;

import java.io.*;
import java.util.Properties;

/**
 * properties 用法
 */
public class PropertiesFactory {
    public static Properties initProperties(String path){
        InputStream inputStream = Properties.class.getClassLoader().getResourceAsStream(path);
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
