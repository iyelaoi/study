package cn.wqz.study.netty.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;

/**
 * Gson（准确） 和 fastJson（json到pojo准确高效）结合使用
 */
public class JsonUtil {

    static GsonBuilder gsonBuilder = new GsonBuilder();
    static{
        gsonBuilder.disableHtmlEscaping();
    }

    public static String pojoToJson(Object object){
        // 这一个可以拿到外边
        String json = gsonBuilder.create().toJson(object);
        return json;
    }

    public static <T>T jsonToPojo(String json, Class<T> tClass){
        T t = JSONObject.parseObject(json, tClass);
        return t;
    }
}
