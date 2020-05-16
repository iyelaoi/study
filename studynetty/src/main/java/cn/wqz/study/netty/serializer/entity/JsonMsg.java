package cn.wqz.study.netty.serializer.entity;

import cn.wqz.study.netty.utils.JsonUtil;

public class JsonMsg {
    private int id;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "JsonMsg{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String convertToJson(){
        return JsonUtil.pojoToJson(this);
    }

    public static JsonMsg parseFromJson(String json){
        return JsonUtil.jsonToPojo(json, JsonMsg.class);
    }
}
