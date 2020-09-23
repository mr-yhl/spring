## 今日内容

+ 注解开发
+ Spring纯注解开发
+ Spring整合单元测试
+ 事务提交

## 第一章 创建model

### 1.创建工程, 引入坐标

> 创建maven model,并引入pom的坐标.分别引入spring框架坐标和junit单元测试的坐标

```xml
<dependencies>
        <!--spring框架-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
        <!--junit单元测试-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>
```

### 2.开发dao

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

### 3.开发service

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

### 4.加入Spring配置文件(重要)

> 新建applicationContext.xml文件,写入配置

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

### 5.测试

> 编写测试文件

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

## 第二章 引入注解(重点)

### 1.对象的创建

#### 1.1复制工程

> 修改复制后medel pow.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring</artifactId>
        <groupId>cn.com.mryhl</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>day02_02_anno_demo</artifactId>
    <dependencies>
        <!--spring框架-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
        <!--junit单元测试-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>

</project>
```

> 修改项目的pow.xml

```xml
<modules>
    <module>day01_01_ioc</module>
    <module>day01_02_springioc</module>
    <module>day01_03_dbutils</module>
    <module>day01_04_crud</module>
    <module>day02_01_review</module>
    <module>day02_02_anno_demo</module>
</modules>
```

#### 1.2添加组件扫描

> 修改applicationContext.xml文件

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

    <!--
    组件扫描, 它需要我们给出一个基础包名
    然后Spring会扫描这个包及其子包下的所有类中的Spring注解, 只有被扫描到的注解才能发挥作用
    -->
    <context:component-scan base-package="cn.com.mryhl"/>
    <!--dao-->
    <!--<bean id="accountDao" class="cn.com.mryhl.dao.impl.AccountDaoImpl"></bean>-->
    <!--service-->
    <!--<bean id="accountService" class="cn.com.mryhl.service.impl.AccountServiceImpl">
        <property name="accountDao" ref="accountDao"></property>
    </bean>-->
</beans>
```

#### 1.3使用注解将对象放入容器


> @Component 这个注解翻译为组件, 只要Spring发现一个类个对象, 并将对象放入自己的IOC容器中,他就会利用当前类反射创建出一dao.impl.AccountDaoImpl"></bean>相当于XML中<bean id="accountDao" class="com.itheima.上面标注了次注解,默认的id是当前类名首字母小写(accountDaoImpl), 也支持使用一个value属性自定义这个id名称


```java
@Component("accountDao")
public class AccountDaoImpl implements AccountDao {
    public void save() {
        System.out.println("保存成功....");
    }
}
```

#### 1.4总结

```markdown
@Component
	用于实例化对象，相当于配置文件中的<bean id="" class=""/>
	它支持一个属性value,相当于xml中bean的id。如果不写，默认值为类名的首字母小写
@Controller  @Service  @Repository
	这三个注解的功能跟@Component类似，他们分别标注在不同的层上。
	@Controller  标注在表现层的类上
	@Service     标注在业务层的类上
	@Repository  标注在持久层的类上
	推荐使用这三个，当一个类实在不好归属在这三个层上时，再使用@Component	
```

### 2.对象生存范围

```markdown
生存范围: singleton  prototype  request  session  globalsession
```

#### 2.1配置

> 单例模式

```java
@Repository
@Scope("singleton") // 指定当前对象为单例(默认)
public class AccountDaoImpl implements AccountDao {
    public void save() {
        System.out.println("保存成功....");
    }
}
```

> 结果
>
> ```markdown
> cn.com.mryhl.dao.impl.AccountDaoImpl@4d826d77
> cn.com.mryhl.dao.impl.AccountDaoImpl@4d826d77
> ```

> 多例模式

```java
@Scope("prototype") // 指定当前对象为多例
public class AccountDaoImpl implements AccountDao {
    public void save() {
        System.out.println("保存成功....");
    }
}
```

> 测试结果
>
> ```markdown
> cn.com.mryhl.dao.impl.AccountDaoImpl@4d826d77
> cn.com.mryhl.dao.impl.AccountDaoImpl@61009542
> ```

> 测试类

```java
public class AccountDaoTest {

    /**
     *测试类
     */
    @Test
    public void test01() throws Exception {
        ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");

        AccountDao accountDao1 = act.getBean(AccountDao.class);
        AccountDao accountDao2 = act.getBean(AccountDao.class);

         System.out.println(accountDao1);
        System.out.println(accountDao2);
    }
}
```

#### 总结

```markdown
@Scope用于指定bean的作用范围(单例和多例)，相当于配置文件中的<bean scope=""> 
```

### 3.对象生命周期

>两个特殊方法

#### 3.1 配置

```java
@Repository
public class AccountDaoImpl implements AccountDao {
    public void save() {
        System.out.println("保存成功....");
    }
    @PostConstruct
    public void init(){
        System.out.println("创建之后");
    }
    @PreDestroy
    public void des(){
        System.out.println("销毁前");
    }
}
// 测试
@Test
    public void test01() throws Exception {
        ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");

        AccountDao accountDao1 = act.getBean(AccountDao.class);
        AccountDao accountDao2 = act.getBean(AccountDao.class);

        System.out.println(accountDao1);
        System.out.println(accountDao2);

        act.close();
    }
```

#### 总结

```markdown
@PostConstruct @PreDestroy 这两个注解标注方法分别在对象的创建之后和销毁之前执行。
相当于<bean init-method="init" destroy-method="destory" /> 
```

### 4.对象依赖注入

#### 4.1@Autowired

```markdown
 @Autowired
表示自动依赖注入,它可以标注在属性上,也可以标注在属性对应的set方法上
当它标注在属性上的时候,set方法可以省略不写

当Spring发现一个类中的属性上标注了 @Autowired  注解的时候, 它会根据当前属性的类型[ AccountDao ] 在Spring的IOC容器中进行查找找不到 就报错 [ expected at least 1 bean which qualifies as autowire candidate.]
找到了一个  就进行依赖注入
找到了多个  它会按照当前属性的名字[accountDao]  进行过滤如果可以过滤出来,就进行依赖注入
如果可以过滤出来,就报错[ expected single matching bean but found 2: accountDaoImpl1,accountDaoImpl2 ]
```

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired    
    private AccountDao accountDao;
    /*public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }*/

    public void save() {
        accountDao.save();
    }
}
```

```markdown
这个注解表示依赖注入，他可以标注在属性上，也可以标注在方法上，当@Autowired标注在属性上的时候，属性对应的set方法可以省略不写
Spring会在他的IOC容器中按照被标注属性的类型进行寻找
	如果找不到，就会报错
	如果找到了，而且正好找到了一个，那么就进行依赖注入
	如果找到了，但是找到了多个，它会再按照属性名称进行匹配
		如果匹配上了就注入
		如果匹配不上就报错
```

#### 4.2@Qualifier

```java
//@Qualifier("accountDao1") 它的作用是配合@Autowired使用, 用于在多个匹配中的类型中按照id选中其中的一个
@Qualifier("accountDao1")
private AccountDao accountDao;
```

```markdown
要跟@Autowired联合使用，代表在按照类型匹配的基础上，再按照名称匹配
```

#### 4.3@Resource(了解)

```markdown
此注解由java提供，而且JDK9版本以后废弃了
Spring会在他的IOC容器中先按照属性名称进行寻找
	如果找到了，就进行依赖注入
	如果找不到，再按照类型进行匹配
		如果正好匹配到一个，就注入
		如果匹配到多个，就报错
注意: @Resource(name = "userDaoImpl1") 如果直接使用name指定名称，他就只会按照名称匹配
```

>面试题: @Autowired (类型--name)    @Resource区别(name-类型)



### 5.注解总结

| xml配置                                               | 注解配置                                       | 说明                                 |
| ----------------------------------------------------- | ---------------------------------------------- | ------------------------------------ |
| < bean id="" class="" >                               | @Component @Controller @Service    @Repository | bean的实例化                         |
| < property name="" ref="">                            | @Autowired  @Qualifier  @Resource              | bean的对象属性注入                   |
| < property name="" value="">                          | @Value                                         | bean的简单属性注入                   |
| < bean scope="">                                      | @Scope                                         | 控制bean的作用范围                   |
| < bean init-method="init" destroy method="destory" /> | @PostConstruct @PreDestroy                     | bean创建之后和销毁之前分别调用的方法 |

>注解
>
>​		**向Spring容器中放入对象 @Component    @Controller    @Service    @Repository**
>
>​		**从Spring容器中获取对象 @Autowired**



## 第三章 使用Spring常用注解版来操作数据库(重点)

> 常用注解版: 我们自己写的类使用注解配置,  第三方的类使用xml配置

### 1.复制工程

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

### 2.删掉XML中dao和service

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

### 3.添加组件扫描

```xml
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
```

### 4.使用注解创建dao

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

### 5.使用注解创建service

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

### 6.测试

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



## 第四章 Spring的纯注解开发(了解)

### 1.开发步骤

#### 1.1 删掉xml中的所有内容

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

#### 1.2创建一个Java类作为配置类

> cn/com/mryhl/config/SpringConfig.java

#### 1.3转移组件扫描

> 注解版的组件扫描,相当于XML中<context:component-scan base-package="com.itheima"/>
>
> bean标签---方法     非bean标签---新注解

```java
@ComponentScan("cn.com.mryhl")
public class SpringConfig {

}
```

#### 1.4转移DataSource

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

#### 1.5 转移QueryRunner

```java
@Bean
public QueryRunner queryRunner(DataSource dataSource){
    return new QueryRunner(dataSource);
}
```

#### 1.6 测试

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



### 2. 优化

#### 2.1优化一: 数据库配置信息提取

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

#### 2.2 优化二:配置类分模块

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

#### 2.3优化三: 配置类

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

### 3.新注解总结

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



## 第五章 Spring整合单元测试(会用)

```markdown
在单元测试中，当点击run的时候，底层工作的其实是一个运行器，这个运行器是junit提供的，它不认识Spring的环境，这也就意味着它无法从spring的容器中获取bean，如果想要从Spring的容器中获取对象，那就必须先认识Spring环境，Spring提供了一个运行器,这个运行器就认识Spring环境, 也就可以获取对象了
```

### 1.导入一个依赖

```xml
<!--Spring单元测试-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.1.6.RELEASE</version>
</dependency>
```

### 2.添加注解配置,然后切换对象获取方式

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

## 第六章完成转账功能

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

##  第七章 解决事务问题

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
</modules>
```

 1.2开发事务管理器

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

#### 1.3 修改dao层代码

```java
@Repository
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private QueryRunner queryRunner;

    @Autowired
    private TxManager txManager;


    public Account findByName(String name) {

        try {
            return queryRunner.query(txManager.getConnection(),"select * from account where name = ?",new BeanHandler<Account>(Account.class),name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update(Account account) {
        try {
            queryRunner.update(txManager.getConnection(),"update account set balance = ? where name = ?",account.getBalance(),account.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
```

 

#### 1.4 修改service层代码

```java
@Repository
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TxManager txManager;

    public void transfer(String sourceAccountName, String targetAccountName, Float amount) {
        try {
            // 事务开启
            txManager.begin();
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
    }
}
```

 1.5测试

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    /**
     * 测试
     */
    @Test
    public void testTranssssfer()  {
        accountService.transfer("B01","B02",10f);
    }
}
```

 2 问题分析

> 现在的事务代码和业务代码严重耦合在一起了，我们希望的是这样：在不改动原来业务代码的前提下，给代码添加事务管理功能
>
> 即：在不修改源代码的情况下，给代码增强功能

### 3.解决思路

动态代理