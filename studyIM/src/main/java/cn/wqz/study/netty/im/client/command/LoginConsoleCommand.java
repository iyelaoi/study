package cn.wqz.study.netty.im.client.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Data
@Service("LoginConsoleCommand")
public class LoginConsoleCommand implements BaseCommand {
    public static final String KEY = "1";

    private String userName;
    private String password;

    @Override
    public void exec(Scanner scanner) {
        System.out.println("please input (username:password)");
        String[] info = null;
        while(true){
            String in = scanner.next();
            info = in.split(":");
            if(info.length != 2){
                System.out.println("format error! please again");
            }else{
                break;
            }
        }
        userName = info[0];
        password = info[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "login";
    }
}
