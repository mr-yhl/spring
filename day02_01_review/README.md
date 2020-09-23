# 回顾

## 1.创建工程, 引入坐标

> 创建maven model,并引入pom的坐标.分别引入spring框架坐标和junit单元测试的坐标

[xml](./pom.xml)

## 2.[开发dao](./src/main/java/cn/com/mryhl/dao)

> 编写dao层的AccountDao接口和它的实现类

```java
// 接口
public interface AccountDao {
    void save();
}
```

```java
// 实现类
public class AccountDaoImpl implements AccountDao {
    public void save() {
        System.out.println("保存成功....");
    }
}
```

## 3.[开发service](./src/main/java/cn/com/mryhl/service)

> service层的接口和实现类

```java
// 接口
public interface AccountService {
    void save();
}
```

```java
// 实现类
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;


    /**
     * 注入对象
     * @param accountDao
     */
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void save() {
        accountDao.save();
    }
}
```

## 4.加入Spring配置文件(重要)

> 新建[applicationContext.xml](./src/main/resources/applicationContext.xml)文件,写入配置

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

    <!--dao-->
    <bean id="accountDao" class="cn.com.mryhl.dao.impl.AccountDaoImpl"></bean>
    <!--service-->
    <bean id="accountService" class="cn.com.mryhl.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"></property>
    </bean>
</beans>
```

## 5.测试

> 编写[测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)

```java
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

// 结果
// 保存成功....
```