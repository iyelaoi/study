package cn.wqz.web.server.utils;

public class ErrorHandler {

    private static Logging logging = Logging.getInstance();

    public static void handle(Exception e){
        logging.error(e.getMessage());
    }

}
