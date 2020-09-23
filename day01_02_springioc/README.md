# Spring的入门案例
## 配置文件
[pom.xml](./pom.xml)
### 1.创建一个模块,并引入spring
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

    <artifactId>day01_02_springioc</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```
### 2.创建dao接口和实现类

```java
/**
 * 创建dao接口
 */
public interface UserDao {
    /**
     * 保存数据
     */
    void save();
}
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("保存成功...");
    }
}
```

### 3. 加入Spring的配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
      <!--
          bean 告诉Spring创建一个对象,将对象放入Spring的IOC容器中
            id    代表的是对象在容器中的唯一标识
            class 代表的是要根据哪一个类去创建这个对象
      -->
      <bean id="userDao" class="cn.com.mryhl.dao.impl.UserDaoImpl"></bean>
</beans>
```

### 4. 创建service实现类

```java
public class UserServiceImpl {
    @Test
    public void save(){
        // 1.读取配置文件,启动spring的IOC容器
        ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 2.从容器中获取对象
        UserDao userDao = (UserDao) act.getBean("userDao");
        // 3.调用对象的方法
        userDao.save();

    }
}
```
## 第五章 API介绍(了解)
### 两个接口(面试题)
#### BeanFactory

这是SpringIOC容器的顶级接口，它定义了SpringIOC的最基础的功能，但是其功能比较简单,  一般面向Spring自身使用

BeanFactroy在第一次使用到某个Bean时(调用getBean())，才对该Bean进行加载实例化

#### ApplicationContext

这是在BeanFactory基础上衍生出的接口，它扩展了BeanFactory的功能，一般面向程序员使用 

ApplicationContext是在容器启动时，一次性创建并加载了所有的Bean

==上面两种方式创建的对象都是单例，只是创建对象的时机不同==

```markdown
BeanFactory和ApplicationContext区别:
	
BeanFactory和ApplicationContext区别:
BeanFactory：是Spring里面最低层的接口，提供了最简单的容器的功能，只提供了实例化对象和拿对象的功能；
ApplicationContext：应用上下文，继承BeanFactory接口，它是Spring的一各更高级的容器，提供了更多的有用的功能；
1) 国际化（MessageSource）
2) 访问资源，如URL和文件（ResourceLoader）
3) 载入多个（有继承关系）上下文 ，使得每一个上下文都专注于一个特定的层次，比如应用的web层  
4) 消息发送、响应机制（ApplicationEventPublisher）
5) AOP（拦截器）
两者装载bean的区别
BeanFactory：
BeanFactory在启动的时候不会去实例化Bean，中有从容器中拿Bean的时候才会去实例化；
ApplicationContext：
ApplicationContext在启动的时候就把所有的Bean全部实例化了。它还可以为Bean配置lazy-init=true来让Bean延迟实例化； 

```

### 三个实现类

这三个类的作用都是：读取配置文件, 初始化Spring的IOC容器,  不一样的是加载配置文件的位置

- ClassPathXmlApplicationContext         读取类路径下的xml作为配置文件
- FileSystemXmlApplicationContext       读取本地目录下的xml作为配置文件
- AnnotationConfigApplicationContext  读取一个Java类作为配置文件

```java
// 1.读取配置文件,启动spring的IOC容器
//ClassPathXmlApplicationContext act = new ClassPathXmlApplicationContext("applicationContext.xml");
FileSystemXmlApplicationContext act = new FileSystemXmlApplicationContext("F:\\code\\spring\\day01_02_springioc\\src\\main\\resources\\applicationContext.xml");
```

### 一个方法

getBean() 用于从Spring容器中获取Bean对象，参数可以使用三种情况：

- getBean("id")                     使用bean的id从容器中查找对象
- getBean(Bean.class)           使用bean的class类型从容器中查找对象
- getBean("id", Bean.class)   使用bean的id 和 class类型从容器中查找对象

```java
// 2.从容器中获取对象
// 根据id获取对象
// UserDao userDao = (UserDao) act.getBean("userDao");
// 根据class获取
// UserDao userDao = act.getBean(UserDao.class);
// 根据id和class获取
UserDao userDao = act.getBean("userDao",UserDao.class);
```

## Bean的配置(重点)

### 1.bean的创建方式

#### 1.1创建对象的三种方式(了解)

> 我们常用的创建对象的方式有三种：
>
> 1. 直接使用new关键字创建
> 2. 使用静态工厂创建
> 3. 使用实例化工厂创建

```java
package cn.com.mryhl.createbeans;

import java.util.*;

public class Book {
    private String name;
    private float price;
    private Date publish;


    //集合, set  get  toString
    private String[] myArray;
    private List<String> myList;
    private Set<String> mySet;

    private Map<String,String> myMap;
    private Properties myProperties;


    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public Date getPublish() {
        return publish;
    }

    public String[] getMyArray() {
        return myArray;
    }

    public void setMyArray(String[] myArray) {
        this.myArray = myArray;
    }

    public List<String> getMyList() {
        return myList;
    }

    public void setMyList(List<String> myList) {
        this.myList = myList;
    }

    public Set<String> getMySet() {
        return mySet;
    }

    public void setMySet(Set<String> mySet) {
        this.mySet = mySet;
    }

    public Map<String, String> getMyMap() {
        return myMap;
    }

    public void setMyMap(Map<String, String> myMap) {
        this.myMap = myMap;
    }

    public Properties getMyProperties() {
        return myProperties;
    }

    public void setMyProperties(Properties myProperties) {
        this.myProperties = myProperties;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPublish(Date publish) {
        this.publish = publish;
    }

    public Book() {
        System.out.println("书正在创建.....");
    }

    //全参构造函数
    public Book(String name, float price, Date publish) {
        this.name = name;
        this.price = price;
        this.publish = publish;

        Date date = new Date();
        this.publish = date;
    }


    public void init(){
        System.out.println("书创建之后....");
    }

    public void des(){
        System.out.println("书销毁之前....");
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", publish=" + publish +
                ", myArray=" + Arrays.toString(myArray) +
                ", myList=" + myList +
                ", mySet=" + mySet +
                ", myMap=" + myMap +
                ", myProperties=" + myProperties +
                '}';
    }
}
```

```java
package cn.com.mryhl.createbeans;

//书厂
//静态工厂创建方式: 不用创建工厂的实例, 直接调用工厂类的一个静态方法来获取对象
//实例化工厂创建方式: 先创建工厂的实例, 再调用工厂实例的一个非静态方法来获取对象

//二者的区别就在于是否去   创建工厂的实例
public class BookFactory {

    //获取书
    public static Book getBook1() {
        return new Book();
    }
    //获取书
    public Book getBook2() {
        return new Book();
    }
}
```

```java
package cn.com.mryhl.createbeans;

public class CreateBean {


    public static void main(String[] args) {
        //我们常用的创建对象的方式有三种：
        //1. 直接使用new关键字创建
        Book book = new Book();

        //2. 使用静态工厂创建
        //需要我们知道1 类名  2 静态方法名
        Book book1 = BookFactory.getBook1();

        //3. 使用实例化工厂创建
        //需要1 先创建工厂实例    2 调用工厂实例的非静态方法
        BookFactory bookFactory = new BookFactory();
        Book book2 = bookFactory.getBook2();
    }
}
```

### 2.直接调用构造函数创建(重点)

```java
package cn.com.mryhl.createbeans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBook {
    public static void main(String[] args) {
        //1. 同时引入多个配置文件
        ClassPathXmlApplicationContext act =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        Book book = (Book) act.getBean("book1");
        
        System.out.println(book);
        

        act.close();
    }
}
```

### 3. 使用静态工厂创建

```xml
<bean id="book" class="cn.com.mryhl.createbeans.Book"></bean>
<!-- 使用静态工厂创建
      需要我们知道1 类名  2 静态方法名
      Book book1 = BookFactory.getBook1();
      class 指定的是工厂的类名
      factory-method   工厂中的静态方法的名称
-->
<bean id="book1" class="cn.com.mryhl.createbeans.BookFactory" factory-method="getBook1">
```

### 4. 使用实例化工厂创建

```xml
<!--使用实例化工厂创建
            需要1 先创建工厂实例    2 调用工厂实例的非静态方法
            BookFactory bookFactory = new BookFactory();
            Book book2 = bookFactory.getBook2();
            factory-bean     用于指定调用哪一个bean工厂对象
            factory-method   用于指定调用工厂中的哪一个非静态方法
   
   -->
<bean id="bookFactory" class="cn.com.mryhl.createbeans.BookFactory"></bean>
<bean id="book2" factory-bean="bookFactory" factory-method="getBook2"></bean>
```

### 5. bean的作用范围(面试)

> 在Spring中，对于bean支持五种作用范围：
>
> - **singleton**(默认) 单例模式，即对象只创建一次, 然后一直存在, 知道Spring容器关闭
> - **prototype**         多例模式，即每次获取bean的时候，IOC都给我们创建一个新对象
> - **request**             web项目中，Spring创建一个Bean的对象，将对象存入到request域中
> - **session**              web 项目中，Spring 创建一个Bean 的对象，将对象存入到session域中
> - **globalSession**   用于分布式web开发中，创建的实例绑定全局session对象

#### 5.1singleton

```xml
<!-- 单例模式,默认就是这个 -->
<bean id="book" class="cn.com.mryhl.createbeans.Book" scope="singleton"></bean>
```

#### 5.2prototype

```xml
<!-- 多例模式 -->
<bean id="book" class="cn.com.mryhl.createbeans.Book" scope="prototype"></bean>
```

### 6. bean的生命周期

> 研究bean的生命周期，无非就是弄明白bean是什么时候创建的，什么时候销毁的
>
> 在Spring中，bean的作用范围会影响到其生命周期，所以我们要分单例和多例对象来研究bean的生命周期

#### 6.1单例对象

生: 容器初始化

死: 容器销毁

#### 6.2多例对象

生: 每一次getBean()调用的时候出生

死: 不会受到Spring的控制, 由垃圾回收期负责回收

```xml
<!--
    Spring在内部内置了两个钩子函数,
    在对象创建之后会去调用init-method属性指定的方法
    在对象销毁之前会去调用destroy-method属性指定的方法
-->
<bean id="book" class="cn.com.mryhl.createbeans.Book"
          scope="prototype"
          init-method="init" destroy-method="des"/>
```

### 7. 依赖注入(重点)

> 依赖注入(Dependency Injection，DI) 其实就是给对象中的属性赋值的过程
>
> 依赖注入有两种方式，分别是使用构造函数和set方法

#### 7.1构造函数

##### 7.1.1在类中提供一个全参构造函数

```java
//全参构造函数
public Book(String name, float price, Date publish) {
    this.name = name;
    this.price = price;
    this.publish = publish;
}
```

##### 7.1.2在配置文件中进行赋值

```xml
<!--
    name  构造函数中的形参的名称
    index 构造函数中的形参的索引, 一般省略
    type  构造函数中的形参的类型,一般可以省略, 底层可以通过反射技术自己获取
    value 构造函数中的参数应该赋的值(简单类型: 基本类型 基本类型包装类型 字符串)
    ref   构造函数中的参数应该赋的值(引用类型) 用于指定当前容器中一个存在的bean对象的id
-->
<bean id="book" class="cn.com.mryhl.createbeans.Book">
    <constructor-arg name="name" value="石头记"></constructor-arg>
    <constructor-arg name="price" value="10"></constructor-arg>
    <constructor-arg name="publish" ref="publish"></constructor-arg>

</bean>
<bean id="publish" class="java.util.Date"></bean>
```

##### 7.1.3测试

```java
public class SpringBook {
    public static void main(String[] args) {
        //1. 同时引入多个配置文件
        ClassPathXmlApplicationContext act =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        Book book = (Book) act.getBean("book");       
        System.out.println(book);
       // Book{name='石头记', price=10.0, publish=Tue Sep 22 17:59:35 CST 2020, myArray=null, myList=null, mySet=null, myMap=null, myProperties=null}

  }
}
```

#### 7.2set方法

##### 7.2.1在类中提供set方法

```java
public void setName(String name) {
    this.name = name;
}

public void setPrice(float price) {
    this.price = price;
}

public void setPublish(Date publish) {
    this.publish = publish;
}
```

##### 7.2.2在配置文件中进行赋值

```xml
<!--
name  set方法中的setXXX部分
value set方法应该赋的值(简单类型: 基本类型 基本类型包装类型 字符串)
ref   set方法应该赋的值(引用类型) 用于指定当前容器中一个存在的bean对象的id
-->
<bean id="book" class="cn.com.mryhl.createbeans.Book">
    <property name="name" value="西游记"></property>
    <property name="price" value="11"></property>
    <property name="publish" ref="publish"></property>
</bean>
<bean id="publish" class="java.util.Date"></bean>
```

##### 7.2.3测试

```java
public class SpringBook {
    public static void main(String[] args) {
        //1. 同时引入多个配置文件
        ClassPathXmlApplicationContext act =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        Book book = (Book) act.getBean("book");       
        System.out.println(book);
       // Book{name='西游记', price=11.0, publish=Tue Sep 22 18:05:25 CST 2020, myArray=null, myList=null, mySet=null, myMap=null, myProperties=null}

  }
}
```



#### 7.3 p名称空间用法

==注意: 使用这个必须导入p的约束==

```xml
<bean id="book" class="cn.com.mryhl.createbeans.Book"
      p:name="西游记"
      p:price="12"
      p:publish-ref="publish"></bean>
<bean id="publish" class="java.util.Date"></bean>
```

### 8 注入集合属性

#### 8.1 准备集合属性

```java
//集合, set  get  toString
private String[] myArray;
private List<String> myList;
private Set<String> mySet;

private Map<String,String> myMap;
private Properties myProperties;
```

#### 8.2单列集合依赖注入

```xml
<bean id="book" class="cn.com.mryhl.createbeans.Book">
    <property name="myArray">
        <array>
            <value>AA</value>
            <value>BB</value>
        </array>
    </property>
    <property name="myList">
        <list>
            <value>CC</value>
            <value>DD</value>
        </list>
    </property>
    <property name="mySet">
        <list>
            <value>EE</value>
            <value>FF</value>
        </list>
    </property>
</bean>
```

#### 8.3双列集合依赖注入

```xml
<property name="myMap">
    <map>
        <entry key="GG" value="gg"></entry>
        <entry key="MM" value="mm"></entry>
    </map>
</property>
<property name="myProperties">
    <props>
        <prop key="XX">xx</prop>
        <prop key="YY">yy</prop>
    </props>
</property>
```



## 配置文件模块化(掌握)

我们现在的配置都集中配在了一个applicationContext.xml文件中，当开发人员过多时， 如果所有bean都配置到同一个配置文件中，会使这个文件巨大。

针对这个问题, Spring给我们提供了两种解决方案:

1. 同时引入多个配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:p="http://www.springframework.org/schema/p"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="book1" class="cn.com.mryhl.createbeans.Book" ></bean>
         
   </beans>
   
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:p="http://www.springframework.org/schema/p"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">
   
       <bean id="book2" class="cn.com.mryhl.createbeans.Book" ></bean>
   
   </beans>
   
   ```

   ```java
   public class SpringBook {
       public static void main(String[] args) {
           //1. 同时引入多个配置文件
           ClassPathXmlApplicationContext act =
                   new ClassPathXmlApplicationContext("bean1.xml","bean2.xml");
   
           Book book1 = (Book) act.getBean("book");
           //Book book2 = (Book) act.getBean("book2");
           System.out.println(book1);
           //System.out.println(book2);
   
           act.close();
       }
   }
   ```

2. 主从配置

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:p="http://www.springframework.org/schema/p"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">
         
         <import resource="bean1.xml"></import>
         <import resource="bean2.xml"></import>
         
   </beans>
   ```

   ```java
   public class SpringBook {
       public static void main(String[] args) {
           //1. 同时引入多个配置文件
           ClassPathXmlApplicationContext act =
                   new ClassPathXmlApplicationContext("beans.xml");
   
           Book book1 = (Book) act.getBean("book");
           //Book book2 = (Book) act.getBean("book2");
           System.out.println(book1);
           //System.out.println(book2);
   
           act.close();
       }
   }
   ```

注意:

1. 同一个xml文件中不允许出现相同名称的bean，如果出现会报错
2. 多个xml文件如果出现相同名称的bean，不会报错，但是后加载的会覆盖前加载，所以尽量保证bean的名称是唯一的。