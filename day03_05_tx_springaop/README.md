[pow.xml](./pom.xml) | [applicationContext.xml](./src/main/resources/applicationContext.xml) | [dao文件夹](./src/main/java/cn/com/mryhl/dao) | [service层](./src/main/java/cn/com/mryhl/service) |
[tx文件](./src/main/java/cn/com/mryhl/tx/TxManager.java) | [测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)
## SpringAOP实现事务管理

### 1. 准备工作

#### 1.1 复制一个工程

> 导入aspectjweaver坐标

```xml
<dependencies>
    <!--aspectj-->
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.8.7</version>
    </dependency>
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

 

#### 1.2 准备目标对象

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TxManager txManager;

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
}
```

 

#### 1.3 准备增强对象

```java
package cn.com.mryhl.tx;

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
        if (connection == null) {
            connection = dataSource.getConnection();
            //从连接池取到连接之后要存入TH里
            threadLocal.set(connection);
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

 

### 2. XML版本

#### 2.1 四大通知版

```xml
<aop:config>
    <!--切点-->
    <aop:pointcut id="pt" expression="execution(* cn.com.mryhl.service.impl.*.*(..))"/>
    <!--切面-->
    <aop:aspect ref="txManager">
        <aop:before method="begin" pointcut-ref="pt"></aop:before>
        <aop:after-returning method="commit" pointcut-ref="pt"></aop:after-returning>
        <aop:after-throwing method="rollback" pointcut-ref="pt"></aop:after-throwing>
        <aop:after method="close" pointcut-ref="pt"></aop:after>
    </aop:aspect>
</aop:config>
```

 

#### 2.2 环绕通知

```java
/**
 * 环绕通知
 */

public void handlerTx(ProceedingJoinPoint pjp){
    try{
        getConnection().setAutoCommit(false);
        pjp.proceed();
        getConnection().commit();
        
    }catch (Throwable e){
        e.printStackTrace();
        try {
            getConnection().rollback();
        } catch (SQLException k) {
            k.printStackTrace();
        }
    }finally {
        try {
            getConnection().close();
            threadLocal.remove();//这里千万别忘记写
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

```xml
<aop:config>
    <!--切点-->
    <aop:pointcut id="pt" expression="execution(* cn.com.mryhl.service.impl.*.*(..))"/>
    <!--切面-->
    <aop:aspect ref="txManager">
        <!--<aop:before method="begin" pointcut-ref="pt"></aop:before>-->
        <!--<aop:after-returning method="commit" pointcut-ref="pt"></aop:after-returning>-->
        <!--<aop:after-throwing method="rollback" pointcut-ref="pt"></aop:after-throwing>-->
        <!--<aop:after method="close" pointcut-ref="pt"></aop:after>-->
        <aop:around method="handlerTx" pointcut-ref="pt"></aop:around>
    </aop:aspect>
</aop:config>
```

 

### 3. 注解版本

#### 3.1 注释掉xml中aop:config

```xml
<!--    <aop:config>
        &lt;!&ndash;切点&ndash;&gt;
        <aop:pointcut id="pt" expression="execution(* cn.com.mryhl.service.impl.*.*(..))"/>
        &lt;!&ndash;切面&ndash;&gt;
        <aop:aspect ref="txManager">
            &lt;!&ndash;<aop:before method="begin" pointcut-ref="pt"></aop:before>&ndash;&gt;
            &lt;!&ndash;<aop:after-returning method="commit" pointcut-ref="pt"></aop:after-returning>&ndash;&gt;
            &lt;!&ndash;<aop:after-throwing method="rollback" pointcut-ref="pt"></aop:after-throwing>&ndash;&gt;
            &lt;!&ndash;<aop:after method="close" pointcut-ref="pt"></aop:after>&ndash;&gt;
            <aop:around method="handlerTx" pointcut-ref="pt"></aop:around>
        </aop:aspect>
    </aop:config>-->
```

####  3.2 激活切面自动代理

```xml
<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
```

#### 3.3 在增强类中配置切面

```java
@Around("execution(* cn.com.mryhl.service.impl.*.*(..))")
public void handlerTx(ProceedingJoinPoint pjp){
    try{
        getConnection().setAutoCommit(false);
        pjp.proceed();
        getConnection().commit();

    }catch (Throwable e){
        e.printStackTrace();
        try {
            getConnection().rollback();
        } catch (SQLException k) {
            k.printStackTrace();
        }
    }finally {
        try {
            getConnection().close();
            threadLocal.remove();//这里千万别忘记写
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

 

#### 3.4 测试

> 事务成功

