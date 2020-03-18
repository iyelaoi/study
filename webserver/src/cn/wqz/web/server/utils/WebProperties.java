package cn.wqz.web.server.utils;

import cn.wqz.web.server.resources.FileResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebProperties extends Properties {

    private static WebProperties webProperties = new WebProperties();

    private WebProperties(){
        File file = new File("web.properties");
        FileResource fileResource = new FileResource(file);
        InputStream inputStream = fileResource.getIn();
        try {
            load(inputStream);
        } catch (IOException e) { // 配置文件加载错误
            ErrorHandler.handle(e);
        }finally {
            fileResource.close();
        }
    }

    public static WebProperties getInstance(){
        return webProperties;
    }
}
