package cn.com.mryhl.test;

import cn.com.mryhl.domain.Account;
import cn.com.mryhl.service.AccountService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class AccountServiceTest {
    // 读取配置文件
    ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
    // 从容器中获取service对象
    AccountService accountService = act.getBean(AccountService.class);

    /**
     * 保存
     */
    @Test
    public void testSave() throws Exception {
        //  调用对象方法
        Account account = new Account();
        account.setName("B02");
        account.setBalance(100f);
        accountService.save(account);
    }

    /**
     * 查找全部
     */
    @Test
    public void testFindAll() throws Exception {
        //  调用对象方法
        List<Account> accountList = accountService.findAll();
        for (Account account : accountList) {
            System.out.println(account);
        }
    }


    /**
     * 查找一个
     */
    @Test
    public void testFindByName() throws Exception {
        //  调用对象方法
        Account account = accountService.findByName("B02");
        System.out.println(account);
    }

    /**
     * 根据name修改
     */
    @Test
    public void testUpdate() throws Exception {
        //  调用对象方法
        Account account = new Account();
        account.setName("B01");
        account.setBalance(200f);

        accountService.update(account);

    }
    /**
     * 查找一个
     */
    @Test
    public void testdele() throws Exception {

        accountService.deleByName("B02");

    }
}
