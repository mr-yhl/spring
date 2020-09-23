package cn.com.mryhl.test;

import cn.com.mryhl.service.AccountService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountServiceTest {

    /**
     *
     */
    @Test
    public void test01() throws Exception {
        ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");

        AccountService accountService = act.getBean(AccountService.class);

        accountService.save();
    }
}
