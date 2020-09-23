# 使用Spring常用注解版来操作数据库(重点)
[dao层文件](./src/main/java/cn/com/mryhl/dao) | [service层文件](./src/main/java/cn/com/mryhl/service)  |  [配置文件](./src/main/java/cn/com/mryhl/config)  | [测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)
| [数据库连接配置](./src/main/resources/db.properties)
> 常用注解版: 我们自己写的类使用注解配置,  第三方的类使用xml配置

## 1.复制工程

```xml
<modules>
    <module>day01_01_ioc</module>
    <module>day01_02_springioc</module>
    <module>day01_03_dbutils</module>
    <module>day01_04_crud</module>
    <module>day02_01_review</module>
    <module>day02_02_anno_demo</module>
    <module>day02_03_crud_anno</module>
</modules>
```

## 2.删掉XML中dao和service
[pom.xml](./pom.xml)
> 2.1 删除多余的

```xml
<!--dao-->
<!--<bean id="accountDao" class="cn.com.mryhl.dao.impl.AccountDaoImpl"></bean>-->
<!--service-->
<!--<bean id="accountService" class="cn.com.mryhl.service.impl.AccountServiceImpl">
    <property name="accountDao" ref="accountDao"></property>
</bean>-->
```

> 2.2 添加引入注解的语句

```xml
<context:component-scan base-package="cn.com.mryhl"></context:component-scan>
```

## 3.添加组件扫描

```xml
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
```

## 4.使用注解创建dao

```java
@Repository
public class AccountDaoImpl implements AccountDao {
    @Autowired // 要注入和被注入的都要在Spring的容器
    private QueryRunner queryRunner;

    // public void setQueryRunner(QueryRunner queryRunner) {
    //     this.queryRunner = queryRunner;
    // }
}
```

## 5.使用注解创建service

```java
@Service
public class AccountServiceImpl implements AccountService {
    //希望Spring给我们注入这个对象
    @Autowired
    private AccountDao accountDao;
    // public void setAccountDao(AccountDao accountDao) {
    //     this.accountDao = accountDao;
    // }
}
```

## 6.测试

```java
@Test
public void testFindAll() throws Exception {
    //  调用对象方法
    List<Account> accountList = accountService.findAll();
    for (Account account : accountList) {
        System.out.println(account);
    }
}
```



# Spring的纯注解开发(了解)

## 1.开发步骤

### 1.1 删掉xml中的所有内容

```xml
<!--<context:component-scan base-package="cn.com.mryhl"></context:component-scan>-->
<!--<bean id="dateSource" class="com.alibaba.druid.pool.DruidDataSource">-->
   <!--    <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>-->
   <!--    <property name="url" value="jdbc:mysql:///spring"></property>-->
   <!--    <property name="username" value="root"></property>-->
   <!--    <property name="password" value="root"></property>-->
   <!--</bean>-->
   <!--<bean id="queryRunner" class="org.apache.commons.dbutils.QueryRunner">-->
   <!--    <constructor-arg name="ds" ref="dateSource"></constructor-arg>-->
   <!--</bean>-->
```

### 1.2创建一个Java类作为配置类

> cn/com/mryhl/config/SpringConfig.java

### 1.3转移组件扫描

> 注解版的组件扫描,相当于XML中<context:component-scan base-package="com.itheima"/>
>
> bean标签---方法     非bean标签---新注解

```java
@ComponentScan("cn.com.mryhl")
public class SpringConfig {

}
```

### 1.4转移DataSource

```java
//@Bean 只能标注方法上, 作用是将当前方法的返回值对象,放入Spring的IOC容器
//@Bean 放入到容器的对象的默认id为当前方法的名字(getDataSource),也可以通过@Bean的value属性指定
//@Bean还具有@Autowired的标注在方法上的时候的所有功能
//如果当前方法需要参数,那么他会自动从Spring的IOC容器中查找,查找顺序跟@Autowired一致
@Bean("datasource")
public DruidDataSource getDataSource(){
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql:///spring");
    dataSource.setUsername("root");
    dataSource.setPassword("root");

    return dataSource;
}
```

### 1.5 转移QueryRunner

```java
@Bean
public QueryRunner queryRunner(DataSource dataSource){
    return new QueryRunner(dataSource);
}
```

### 1.6 测试

> 修改了引入配置文件的部分

```java
public class AccountServiceTest {
    // 读取配置文件
    // ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
    ApplicationContext act = new AnnotationConfigApplicationContext(SpringConfig.class);
    // 从容器中获取service对象
    AccountService accountService = act.getBean(AccountService.class);

    
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
```



## 2. 优化

### 2.1优化一: 数据库配置信息提取

> db.properties

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql:///spring
jdbc.username=root
jdbc.password=root
```

> @PropertySource("db.properties") 引入*.properties文件

```java
/**
 * Java文件作为配置文件
 * 注解版的组件扫描,相当于XML中<context:component-scan base-package="com.itheima"/>
 * bean标签---方法     非bean标签---新注解
 */
@ComponentScan("cn.com.mryhl")
@PropertySource("db.properties")
public class SpringConfig {

    //@Value 用于给类中的简单类型的属性进行依赖注入
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    //@Bean 只能标注方法上, 作用是将当前方法的返回值对象,放入Spring的IOC容器
    //@Bean 放入到容器的对象的默认id为当前方法的名字(getDataSource),也可以通过@Bean的value属性指定
    //@Bean还具有@Autowired的标注在方法上的时候的所有功能
    //如果当前方法需要参数,那么他会自动从Spring的IOC容器中查找,查找顺序跟@Autowired一致
    @Bean("datasource")
    public DruidDataSource getDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
    @Bean
    public QueryRunner queryRunner(DataSource dataSource){
        return new QueryRunner(dataSource);
    }

}
```

### 2.2 优化二:配置类分模块

> 新建DbConfig.java文件

```java
@PropertySource("db.properties")
public class DbConfig {
    //@Value 用于给类中的简单类型的属性进行依赖注入
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    //@Bean 只能标注方法上, 作用是将当前方法的返回值对象,放入Spring的IOC容器
    //@Bean 放入到容器的对象的默认id为当前方法的名字(getDataSource),也可以通过@Bean的value属性指定
    //@Bean还具有@Autowired的标注在方法上的时候的所有功能
    //如果当前方法需要参数,那么他会自动从Spring的IOC容器中查找,查找顺序跟@Autowired一致
    @Bean("datasource")
    public DruidDataSource getDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
```

> 修改SpringConfig.java文件,引入DbConfig.java文件

```java
@ComponentScan("cn.com.mryhl")
@Import(DbConfig.class)
public class SpringConfig {


    @Bean
    public QueryRunner queryRunner(DataSource dataSource){
        return new QueryRunner(dataSource);
    }

}
```

### 2.3优化三: 配置类

> 优化了主配置java文件中大量引用的问题

```java
//@Configuration 此注解标注在一个类上,代表此类是一个配置类
//Spring在IOC容器启动的时候,会自动扫描所有的配置类, 然后会自动执行类中的方法
@Configuration
@PropertySource("db.properties")
public class DbConfig {
    ...
}
```

## 3.新注解总结

```markdown
@ComponentScan
	组件扫描注解。 相当于xml配置文件中的<context:component-scan base-package=""/> 

@Bean(重点)
	该注解只能写在方法上，表明使用此方法创建一个对象，并且放入spring容器。它支持一个name属性，用于给生成的bean取一个id。 

@PropertySource
	用于引入其它的properties配置文件

@Import
	在一个配置类中导入其它配置类的内容

@Configuration
	被此注解标注的类,会被Spring认为是配置类。Spring在启动的时候会自动扫描并加载所有配置类，然后将配置类中bean放入容器

```



# Spring整合单元测试(会用)

```markdown
在单元测试中，当点击run的时候，底层工作的其实是一个运行器，这个运行器是junit提供的，它不认识Spring的环境，这也就意味着它无法从spring的容器中获取bean，如果想要从Spring的容器中获取对象，那就必须先认识Spring环境，Spring提供了一个运行器,这个运行器就认识Spring环境, 也就可以获取对象了
```

## 1.导入一个依赖

```xml
<!--Spring单元测试-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.1.6.RELEASE</version>
</dependency>
```

## 2.添加注解配置,然后切换对象获取方式

```java
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
```
