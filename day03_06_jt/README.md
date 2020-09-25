## JdbcTemplate介绍(会用)
[pom.xml](./pom.xml) | [Account](./src/main/java/cn/com/mryhl/domain/Account.java) | 
[applicationContext.xml](./src/main/resources/applicationContext.xml) | [dao文件夹](./src/main/java/cn/com/mryhl/dao) | [service层](./src/main/java/cn/com/mryhl/service) |
 [测试文件](./src/test/java/test/AccountServiceTest.java)

### 1. JdbcTemplate介绍

JdbcTemplate是Spring提供的持久层技术，用于操作数据库，它底层封装了JDBC技术。

核心类：

- JdbcTemplate  用于执行增删改查的SQL语句 (QueryRunner)
- RowMapper  这是一个接口，主要作用是将数据库返回的记录封装进实体对象(ResultHandler)

核心方法：

- update()  用来执行增、删、改语句
- query()    用来执行查询语句

```java
//创建一个JdbcTemplate对象，用来执行增删改查, 需要给一个数据源
JdbcTemplate  jdbcTemplate = new JdbcTemplate(dataSource);

//update方法，用于执行增删改语句
//第一个参数:sql语句   后面的参数:sql语句中的所需要的的值
jdbcTemplate.update("insert into account value(null,?,?)",1,2);

//query或者queryForObject方法，用于执行查询语句
//query 用于查询多条记录,返回一个集合   queryForObject用于查询一条记录,返回一个实体
//第一个参数:sql语句   第二个参数:封装返回值   后面的参数:sql语句中的所需要的的值
jdbcTemplate.query("select * from account", new BeanPropertyRowMapper<Account>(Account.class)); 
jdbcTemplate.queryForObject("select * from account where aid = ?",  new BeanPropertyRowMapper<Account>(Account.class),  1);
```

### 2. JdbcTemplate案例

使用JdbcTemplate完成一个crud+转账的案例(常用注解版)

#### 2.1 准备数据环境

 

| aid  | name | balance |
| ---- | ---- | ------- |
| 1    | B01  | 100.00  |
| 2    | B02  | 100.00  |



#### 2.2 创建一个工程,导入依赖

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
        <!--spring-jdbc-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
        <!--spring-context-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
        <!--aspectjweaver-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.7</version>
        </dependency>
        <!--junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <!--spring-test-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
    </dependencies>
```

#### 2.3 创建实体类

```java
package cn.com.mryhl.domain;

public class Account {
    private Integer aid;
    private String name;
    private Float balance;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "aid=" + aid +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
```

 

#### 2.4 创建dao接口

```java
public interface AccountDao {
    /**
     * 保存
     */
    void save(Account account);

    /**
     * 查询所有
     */
    List<Account> findAll();

    /**
     * 根据name查询
     */
    Account findByName(String name);

    /**
     * 根据name修改
     */
    void update(Account account);

    /**
     * 删除
     */
    void deleByName(String name);
}
```

 

#### 2.5 创建dao实现类

```java
@Repository
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void save(Account account) {

        jdbcTemplate.update("insert into account values(null,?,?)", account.getName(), account.getBalance());

    }

    public List<Account> findAll() {

        return jdbcTemplate.query("select * from account", new BeanPropertyRowMapper<Account>(Account.class));

    }

    public Account findByName(String name) {

        return jdbcTemplate.queryForObject("select * from account where name = ?", new BeanPropertyRowMapper<Account>(Account.class), name);


    }

    public void update(Account account) {

        jdbcTemplate.update("update account set balance = ? where name = ?", account.getBalance(), account.getName());

    }

    public void deleByName(String name) {

        jdbcTemplate.update("delete from account where name = ?", name);
    }


}
```

 

#### 2.6 创建service接口

```java
public interface AccountService {
    /**
     * 保存
     */
    void save(Account account);

    /**
     * 查询所有
     */
    List<Account> findAll();

    /**
     * 根据name查询
     */
    Account findByName(String name);

    /**
     * 根据name修改
     */
    void update(Account account);

    /**
     * 删除
     */
    void deleByName(String name);
}
```

 

#### 2.7 创建service实现类

```java
public void transfer(String sourceAccountName, String targetAccountName, Float amount) {
    // 查询两个用户的信息
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
```

 

#### 2.8 加入Spring的配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
             http://www.springframework.org/schema/beans/spring-beans.xsd
             http://www.springframework.org/schema/context
             http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">
    <!--组件扫描-->
    <context:component-scan base-package="cn.com.mryhl"/>

    <!--dataSource-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql:///spring"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>

    <!--JdbcTemplate-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
</beans>
```

 

#### 2.9 测试

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    
    /**
     * 
     */
    @Test
    public void test01(){
        accountService.transfer("B01","B02",10f);       
    }

}
```

 



##  第九章 Spring中的事务管理(了解)

### 1. 事务管理方式

Spring支持两种事务管理方式：编程式事务和声明式事务

- 编程式事务就是将业务代码和事务代码放在一起书写，它的耦合性太高，开发中不使用

- 声明式事务其实就是将事务代码和业务代码隔离开发，然后通过一段配置让他们组装运行，最后达到事务控制的目的


声明式事务就是通过AOP原理实现的

### 2. Spring事务管理相关的API

#### 2.1 PlatformTransactionManager 

PlatformTransactionManager这是Spring进行事务管理的一个根接口，我们要使用它的实现类做事务管理

**我们需要知道的是:mybatis和jdbcTemplate都可以使用它的一个子类(DataSourceTransactionManager)做事务管理**

#### 2.2 TransactionDefinition

TransactionDefinition这个API是用来做事务定义的

| 内容                                                         | 分类         |
| ------------------------------------------------------------ | ------------ |
| is ReadOnly0 : boolean                                       | 事务是否只读 |
| ISOLATION_DEFAULT : int = - 1<br/>ISOLATION_READ_COMMITTED:int=2<br/>ISOLATION_READ_UNCOMMITTED:int=1<br/>ISOLATION_REPEATABLE_READ:int=4<br/>ISOLATION_SERIALIZABLE:int=8 | 事务隔离级别 |
| PROPAGATION MANDATORY : int = 2<br/>PROPAGATION NESTED : int = 6<br/>PROPAGATION NEVER : int = 5<br/>PROPAGATION NOT SUPPORTED: int=4<br/>PROPAGATION REQUIRED:int=0<br/>PROPAGATION_REQUIRES_NEW:int=3<br/>PROPAGATION SUPPORTS : int = 1 | 事务传播行为 |
| TIMEOUT_DEFAULT:int=-1                                       | 事务超时时长 |

##### 2.3 隔离级别

>事务隔离级别相关【不设置事务隔离级别，可能引发脏读、不可重复读、虚读】
>
>+ ISOLATION_READ_UNCOMMITTED读未提交
>
>+ ISOLATION_READ_COMMITTED读已提交
>
>+ ISOLATION_REPEATABLE_READ可重复度
>
>+ ISOLATION_SERIALIZABLE串行化
>
>  mysq1支持四种，默认可重复度
>  oracle支持两种（读已提交和串行化）,默认是读已提交

 

##### 2.4 传播行为

事务传播行为指的就是当一个业务方法【被】另一个业务方法调用时，应该如何进行事务控制 

```java
a(){
    
    b();// b必须有事务才可以运行
}


b(){}
```

> + PROPAGATION_REQUIRED(必须有事务，这是默认值）
>   如果存在一个事务，则加入到当前事务。如果没有事务则开启一个新的事务。
> + PROPAGATION_REQUIRES_NEW(必须有新的）
>   总是开启一个新的事务。如果存在一个事务，则将这个存在的事务挂起，再来一个新的。
> + PROPAGATION_SUPPORTS(支持有事务）
>   如果存在一个事务，则加入到当前事务。如果没有事务则非事务运行。
> + PROPAGATION_NOT_SUPPORTED(不支持有事务）
>   总是非事务地执行，并挂起任何存在的事务。
>   
>   
> + PROPAGATION_MANDATORY(强制有事务，自己还不负责创建）
>   如果存在一个事务，则加入到当前事务。如果没有事务，则抛出异常。
> + PROPAGATION_NEVER(强制不要事务，自己还不负责挂起）:
>   总是非事务地执行,如果存在一个活动事务,则抛出异常



##### 2.4.1只读性

只读事务(增 删  改不能使用,只能查询使用)

换句话说,只读事务只能用于查询方法

##### 2.4.2 超时时长

事务超时时间, 此属性需要底层数据库的支持

它的默认值是-1, 代表不限制

#### 2.5TransactionStatus

TransactionStatus代表的是事务的当前状态

#### 2.6 三个API之间的关系

PlatformTransactionManager通过读取TransactionDefinition中定义事务信息参数,来管理事务,

管理之后会产生一些列的TransactionStatus

