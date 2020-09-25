##  解决事务问题
[pow.xml](./pom.xml) | [applicationContext.xml](./src/main/resources/applicationContext.xml) | [dao文件夹](./src/main/java/cn/com/mryhl/dao) | [service层](./src/main/java/cn/com/mryhl/service) |
[tx文件](./src/main/java/cn/com/mryhl/tx/TxManager.java) | [测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)
### 1.代码开发

#### 1.1复制工程

```xml
<modules>
    <module>day01_01_ioc</module>
    <module>day01_02_springioc</module>
    <module>day01_03_dbutils</module>
    <module>day01_04_crud</module>
    <module>day02_01_review</module>
    <module>day02_02_anno_demo</module>
    <module>day02_03_crudanno</module>
    <module>day02_04_transfer</module>
    <module>day02_05_tx</module>
    <module>day03_01_tx_jdk</module>
</modules>
```

 1.2准备代理逻辑

```java
package com.itheima.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class TxManager {

    //本地集合
    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    @Autowired
    private DataSource dataSource;

    //获取到Connection
    public Connection getConnection() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection ** null) {
            connection = dataSource.getConnection();
            threadLocal.set(connection);//从连接池取到连接之后要存入TH里
        }
        return connection;
    }

    //开启事务
    public void begin(){
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //提交事务
    public void commit(){
        try {
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //回滚事务
    public void rollback(){
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //关闭事务
    public void close(){
        try {
            getConnection().close();
            threadLocal.remove();//这里千万别忘记写
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

#### 1.3 准备目标对象

```java
@Repository
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TxManager txManager;

    public void transfer(String sourceAccountName, String targetAccountName, Float amount) {
        Account sourceAccount = accountDao.findByName(sourceAccountName);
        Account targetAccount = accountDao.findByName(targetAccountName);
        // 内存内做金额变更
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        // 变更后的金额提交
        accountDao.update(sourceAccount);
        // int i=1/0;
        accountDao.update(targetAccount);
    }
}
```

 

#### 1.4 生成代理对象

```java
/**
 * 引入spring测试注解
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    /**
     * 引入事务
     */
    @Autowired
    private TxManager txManager;
    /**
     * 进行测试
     */
    @Test
    public void testTranssssfer()  {
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
        // 使用jdk的动态代理产生代理对象
        Proxy.newProxyInstance(
                accountService.getClass().getClassLoader(),
                accountService.getClass().getInterfaces(),
                invocationHandler
                );

        accountService.transfer("B01","B02",10f);
    }
}
```
