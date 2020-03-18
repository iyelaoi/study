package cn.wqz.websplit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.wqz.websplit.dao")
public class WebsplitApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsplitApplication.class, args);
    }

}
