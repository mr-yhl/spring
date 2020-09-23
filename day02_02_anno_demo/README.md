# 引入注解(重点)

## 1.对象的创建

### 1.1复制工程

> 修改复制后medel [pow.xml](./pom.xml)

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

### 1.2添加组件扫描

> 修改[applicationContext.xml](./src/main/resources/applicationContext.xml)文件

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

### 1.3使用注解将对象放入容器


> @Component 这个注解翻译为组件, 只要Spring发现一个类个对象, 并将对象放入自己的IOC容器中,他就会利用当前类反射创建出一dao.impl.AccountDaoImpl"></bean>相当于XML中<bean id="accountDao" class="com.itheima.上面标注了次注解,默认的id是当前类名首字母小写(accountDaoImpl), 也支持使用一个value属性自定义这个id名称


```java
@Component("accountDao")
public class AccountDaoImpl implements AccountDao {
    public void save() {
        System.out.println("保存成功....");
    }
}
```

### 1.4总结

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

## 2.对象生存范围

```markdown
生存范围: singleton  prototype  request  session  globalsession
```

### 2.1[配置](./src/main/java/cn/com/mryhl/dao/impl/AccountDaoImpl.java)

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

> [测试类](./src/test/java/cn/com/mryhl/test/AccountDaoTest.java)

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

### 总结

```markdown
@Scope用于指定bean的作用范围(单例和多例)，相当于配置文件中的<bean scope=""> 
```

## 3.对象生命周期

>两个特殊方法

### 3.1 配置

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

### 总结

```markdown
@PostConstruct @PreDestroy 这两个注解标注方法分别在对象的创建之后和销毁之前执行。
相当于<bean init-method="init" destroy-method="destory" /> 
```

## 4.对象依赖注入

### 4.1@Autowired

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

### 4.2@Qualifier

```java
//@Qualifier("accountDao1") 它的作用是配合@Autowired使用, 用于在多个匹配中的类型中按照id选中其中的一个
@Qualifier("accountDao1")
private AccountDao accountDao;
```

```markdown
要跟@Autowired联合使用，代表在按照类型匹配的基础上，再按照名称匹配
```

### 4.3@Resource(了解)

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



## 5.注解总结

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

