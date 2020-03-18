package cn.wqz.web.server.utils;

/**
 * 异常处理类
 */
public class ExceptionHandler {

    /**
     * 打印异常
     * @param e
     */
    public static void handle(Exception e){
        System.out.println(e.getMessage());
    }

}
