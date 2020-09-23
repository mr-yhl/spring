package cn.com.mryhl.createbeans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBook {
    public static void main(String[] args) {
        //1. 同时引入多个配置文件
        ClassPathXmlApplicationContext act =
                new ClassPathXmlApplicationContext("beans.xml");

        Book book1 = (Book) act.getBean("book");
        //Book book2 = (Book) act.getBean("book2");
        System.out.println(book1);
        //System.out.println(book2);

        act.close();
    }
}
