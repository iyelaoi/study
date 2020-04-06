package cn.wqz.thread.model;

public enum StringEnum {

    V1("v1"), V2("v2"), V3("v3");

    private String var;

    private StringEnum(String var){
        this.var = var;
    }

    public String getVar(){
        return var;
    }

}
