## 完成转账功能
[pow.xml](./pom.xml) | [applicationContext.xml](./src/main/resources/applicationContext.xml) | [dao文件夹](./src/main/java/cn/com/mryhl/dao) | [service层](./src/main/java/cn/com/mryhl/service) |
[tx文件](./src/main/java/cn/com/mryhl/tx/ThreadLocal.java) | [测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)
### 1.思路分析

> 转账问题,一个用户向另一个用户转账.一个金额加一个金额减.当在一个减掉金额后发生异常,另一个未加,则出现问题.
>
> 需要这两个同时进行或不进行.

### 2.代码开发

#### 2.1准备数据库环境

| aid  | name | balance |
| ---- | ---- | ------- |
| 1    | B01  | 100.00  |
| 2    | B02  | 100.00  |

#### 2.2创建新的模块,导入依赖

```xml
<dependencies>
    <!--mysql-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
    </dependency>
    <!--druid-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.1.15</version>
    </dependency>
    <dependency>
        <groupId>commons-dbutils</groupId>
        <artifactId>commons-dbutils</artifactId>
        <version>1.7</version>
    </dependency>
    <!--spring-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.1.6.RELEASE</version>
    </dependency>
    <!--junit-->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.1.6.RELEASE</version>
    </dependency>
</dependencies>
```

 

#### 2.3创建实体类

> 创建实体,对应于数据库中的字段

```java
public class Account {
    private Integer aid;
    private String name;
    private Float balance;
}
```

 2.4开发dao接口

```java
public interface AccountDao {
    /**
     * 查询
     */
    Account findByName(String name);
    /**
     * 更新
     */
    void update(Account account);
}
```

 2.5开发dao实现类

> 通过注解引入QueryRunner

```java
@Repository
public class AccountDaoImpl implements AccountDao {
    
    @Autowired
    private QueryRunner queryRunner;    
    
    public Account findByName(String name) {

        try {
            return queryRunner.query("select * from account where name = ?",new BeanHandler<Account>(Account.class),name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Account account) {
        try {
            queryRunner.update("update account set balance = ? where name = ?",account.getBalance(),account.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
```

 

#### 2.6开发service接口

> 定义抽象类

```java
public interface AccountService {
    /**
     * 转账抽象类
     * @param sourceAccountName 转账用户
     * @param targetAccountName 目标用户
     * @param amount 金额
     */
    void transfer(String sourceAccountName,String targetAccountName,Float amount);
}
```

 

#### 2.7开发service实现类

> 做金额变更

```java
@Repository
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    public void transfer(String sourceAccountName, String targetAccountName, Float amount) {
        // 查询两个用户的信息
        Account sourceAccount = accountDao.findByName(targetAccountName);
        Account targetAccount = accountDao.findByName(targetAccountName);
        // 内存内做金额变更
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        // 变更后的金额提交
        accountDao.update(sourceAccount);
        accountDao.update(targetAccount);
    }
}
```

 

#### 2.8加入Spring的配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
             http://www.springframework.org/schema/beans/spring-beans.xsd
             http://www.springframework.org/schema/context
             http://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="cn.com.mryhl"></context:component-scan>
    <bean id="dateSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql:///spring"></property>
        <property name="username" value="root"></property>
        <property name="password" value="root"></property>
    </bean>
    <bean id="queryRunner" class="org.apache.commons.dbutils.QueryRunner">
        <constructor-arg name="ds" ref="dateSource"></constructor-arg>
    </bean>
</beans>
```

 2.9测试

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
     *
     */
    @Test
    public void testTranssssfer()  {
        accountService.transfer("B01","B02",10f);
    }
}
```

 

### 3.问题分析

> 代码存在事务问题，事务问题原因是:  每执行完一条sql语句后， jdbc会自动提交事务
>
> 如果想要控制事务就要关闭事务的自动提交，而选择手动提交事务
>
> 要手动提交事务，就要保证执行多条sql语句的是同一个connection

### 4.解决思路

> 想办法让同一个业务中的所有sql使用同一个connection

```markdown
让同一个业务中的所有SQL语句使用同一个连接，也就是说：当一个业务中有多条SQL的时候
不是每执行一条SQL就去连接池获取一个连接，用完了就归还
而是在第一条SQL执行之前获取到连接，用完了暂不归还，而是等到所有的SQL都执行完毕再归还
```

> 想办法禁止自动提交，然后手动控制事务的提交和回滚

```markdown
开发一个事务管理器的类，在里面手动控制事务的开启和关闭
准备一个本地集合
然后接下来，在所有的业务sq执行之前，手动的获取一个连接存储到本地集合
下面所有的sq|运行过程中都使用本地集合中的连接对象，
知道所有sql全部执行完毕之后，我们再提交事务，然后归还连接到连接池
这个本地集合应该还有这样一种能力：
一个线程存进去的对象只有这个线程可以看见（取出来）
```

### 5.ThreadLocal介绍

> 本地局部变量，它的底层就是一个特殊的map结构，此map的键是固定的，是当前线程对象
>
> 常见方法:
>
> ​	set(value)   向ThreadLocal中存入值
>
> ​	get()           从ThreadLocal取出值
>
> ​	remove()    移除ThreadLocal中跟当前线程相关的数据

```java
/**
 * 本地集合
 */
public class ThreadLocal {
    private Map<Thread,String> map = new Hashtable<Thread, String>();

    /**
     * 保存
     */
    public void set(String value){
        map.put(Thread.currentThread(),value);
    }

    /**
     * 获取
     */
    public String get(){
        return map.get(Thread.currentThread());
    }
    /**
     * 清理
     */
    public String remove(){
        return map.remove(Thread.currentThread());
    }
}
```