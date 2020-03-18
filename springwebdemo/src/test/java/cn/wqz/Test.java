package cn.wqz;


import cn.wqz.service.IService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    @org.junit.Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        IService service = (IService) context.getBean("iService");
        service.doSome();
    }
}
