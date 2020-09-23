package cn.com.mryhl.test;

import cn.com.mryhl.config.SpringConfig;
import cn.com.mryhl.domain.Account;
import cn.com.mryhl.service.AccountService;
import cn.com.mryhl.service.impl.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class) //切换运行器
@ContextConfiguration(classes = SpringConfig.class)//向运行器传递配置文件的位置
public class AccountServiceTest {
    // 读取配置文件
    // ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
    // ApplicationContext act = new AnnotationConfigApplicationContext(SpringConfig.class);
    // 从容器中获取service对象
    // AccountService accountService = act.getBean(AccountService.class);
    @Autowired
    private AccountService accountService;
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


}
