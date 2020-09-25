package cn.com.mryhl.test;

import cn.com.mryhl.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountServiceTest {
    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContest.xml");

        AccountService accountService = act.getBean(AccountService.class);
        accountService.findAll();
    }
}
