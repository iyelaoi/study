package cn.wqz.web.server.utils;

import java.util.Date;

/**
 * 日志
 */
public class Logging {

    private static Logging logging;

    private Logging(){}

    public static Logging getInstance(){
        if(logging == null){
            synchronized (Logging.class){
                if(logging == null){
                    logging = new Logging();
                }
            }
        }
        return logging;
    }

    public void info(String message){
        print("info", message);
    }

    public void debug(String message){
        print("debug", message);
    }

    public void error(String message){
        print("error", message);
    }

    public void warn(String message){
        print("warn", message);
    }

    /**
     *
     * @param logType
     * @param message
     */
    private void print(String logType, String message){
        System.out.println("[" + new Date().toString()  +  "] " + logType + ": " + message);
    }
}
