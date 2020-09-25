package cn.com.mryhl.test;


import cn.com.mryhl.service.impl.AccountServiceImpl;
import cn.com.mryhl.tx.TxManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.lang.reflect.Method;


/**
 * 引入spring测试注解
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AccountServiceTest {
    @Autowired
    private AccountServiceImpl accountService;

    /**
     * 引入事务
     */
    @Autowired
    private TxManager txManager;
    /**
     * 进行测试
     */
    @Test
    public void testTransfer()  {
        // 编写代理逻辑
        InvocationHandler invocationHandler = new InvocationHandler(){
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 创建返回对象
                Object obj = null;
                try {
                    // 事务开启
                    txManager.begin();
                    // 查询两个用户的信息
                    obj = method.invoke(accountService,args);
                    // 事务提交
                    txManager.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 事务回滚
                    txManager.rollback();
                } finally {
                    // 事务关闭
                    txManager.close();
                }

                return obj;
            }
        };
        // 使用Cglib的动态代理产生代理对象
        // 1. 创建一个增强器
        Enhancer enhancer = new Enhancer();
        // 2. 设置父类
        enhancer.setSuperclass(AccountServiceImpl.class);
        // 3. 设置代理逻辑
        enhancer.setCallback(invocationHandler);
        // 4. 产生代理对象
        AccountServiceImpl instance = (AccountServiceImpl) enhancer.create();

        instance.transfer("B01","B02",10f);
    }
}
