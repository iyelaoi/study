package cn.wqz.study.json.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonMsgTester {
    public JsonMsg buildMsg(){
        JsonMsg jsonMsg = new JsonMsg();
        jsonMsg.setId(1000);
        jsonMsg.setContent("曾经沧海难为水，除去巫山不是云！");
        return jsonMsg;
    }

    public static void main(String[] args) {
        new JsonMsgTester().serAndDeser();
    }
    public void serAndDeser(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JsonMsg jsonMsg = buildMsg();
        String json = gson.toJson(jsonMsg);
        System.out.println("json = " + json);
        JsonMsg inMsg = JSONObject.parseObject(json, JsonMsg.class);
        System.out.println("from json:" + inMsg);
    }

}
