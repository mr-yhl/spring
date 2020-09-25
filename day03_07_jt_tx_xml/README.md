
[pom.xml](./pom.xml) | [Account](./src/main/java/cn/com/mryhl/domain/Account.java) | 
[applicationContext.xml](./src/main/resources/applicationContext.xml) | [dao文件夹](./src/main/java/cn/com/mryhl/dao) | [service层](./src/main/java/cn/com/mryhl/service) |
 [测试文件](./src/test/java/test/AccountServiceTest.java)

## 声明式事务(重点)

### 1. 思路

目标对象:  业务层的所有对象   **自己开发**

增强对象:  事务管理器 (DataSourceTransactionManager) 

​                **需要我们给它传递一批参数(事务隔离级别  传播行为  超时时长  只读)**

配置切面:  配置的是切点和增强的关系   **自己配置**

### 2. xml版(重点)

#### 2.1 复制工程

 

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
    <module>day03_02_tx_cglib</module>
    <module>day03_03_aop</module>
    <module>day03_04_aop_anno</module>
    <module>day03_05_tx_springaop</module>
    <module>day03_06_jt</module>
    <module>day03_07_jt_tx_xml</module>
</modules>
```

#### 2.2 准备目标对象

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

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

    public void save(Account account) {
        accountDao.save(account);
    }

    public List<Account> findAll() {
        return accountDao.findAll();
    }

    public Account findByName(String name) {
        return accountDao.findByName(name);
    }

    public void update(Account account) {
        accountDao.update(account);
    }


    public void deleByName(String name) {
        accountDao.deleByName(name);
    }
}
```

 

#### 2.3 配置事务管理器的参数和切面

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
             http://www.springframework.org/schema/beans/spring-beans.xsd
             http://www.springframework.org/schema/context
             http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
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
    <!--事务管理器-->
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="find*" read-only="true"></tx:method>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>
    <aop:config>
        <aop:pointcut id="pt" expression="execution(* cn.com.mryhl.service.impl.*.*(..))"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pt"></aop:advisor>
    </aop:config>
</beans>
```

 

#### 2.4 事务管理器参数的配置

```markdown
<tx:attributes>
<!--控制查询方法, 事务是只读的-->
<!--
name作用是在所有的切点中进行二次匹配, 通过规则挑选一部分  这个匹配从上往下执行的,一旦匹配到一个,就不再匹配下面的了
isolation="DEFAULT"  设置隔离级别
propagation="REQUIRED"  设置传播行为
timeout="-1"    设置超时时间
read-only="false"    设置是否只读
rollback-for=""       只针对什么异常回滚
no-rollback-for=""    对于哪个异常不回滚
-->
<tx:method name="find*" read-only="true"/>
<tx:method name="*"/>
</tx:attributes>
```

 