package cn.com.mryhl.test;

import cn.com.mryhl.dao.AccountDao;
import cn.com.mryhl.service.AccountService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountDaoTest {

    /**
     *
     */
    @Test
    public void test01() throws Exception {
        ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");

        AccountDao accountDao1 = act.getBean(AccountDao.class);
        AccountDao accountDao2 = act.getBean(AccountDao.class);

        System.out.println(accountDao1);
        System.out.println(accountDao2);

        act.close();
    }
}
