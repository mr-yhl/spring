## 认识IOC
我们今天想通过service和dao层调用来推导IOC

### 1. 环境准备

1. 创建一个新的MAVEN工程,干掉src目录

2. 新建一个module,引入坐标

```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 2.版本一：原始版本

#### 2.1创建dao接口
[dao](./src/main/java/cn/com/mryhl/dao/UserDao.java)

#### 2.2创建dao实现类

[daoIMpl](./src/main/java/cn/com/mryhl/dao/impl/UserDaoImpl.java)

#### 2.3在service中创建dao

[service文件](./src/main/java/cn/com/mryhl/service/impl/UserServiceImpl.java)

> service层和dao层代码耦合了

### 3.版本二：工厂解耦

#### 3.1创建一个生产对象的工厂

[配置文件](./src/main/resources/beans.properties)

#### 3.2使用工厂创建对象

```java
public class BeanFactory {
    // 创建对象
    public static Object getBean(String beanId){

        try {
            // 读取配置文件
            ResourceBundle rb = ResourceBundle.getBundle("beans");
            // 获取类的权限定名
            String className = rb.getString(beanId);
            // 反射创建对象
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.newInstance();

            return instance;
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }
}
```

#### 问题

> 每次都会创建一个dao对象，对象是多例的，浪费了资源；而且每次使用的时候才创建，浪费了时间
>
> 希望对象可以提前创建好一个， 存储起来，等我们调用的时候，直接返回

### 4.版本三：优化工厂
[工厂类](./src/main/java/cn/com/mryhl/factory/BeanFactory.java)

### 5.总结(IOC概念引出)

> 对象的创建由原来的==使用new关键字在类中主动创建==变成了==从工厂中获取==，而对象的创建过程由工厂内部来实现，
>
> 而这个工厂就类似Spring的IOC容器，也就是以后我们的==对象不再自己创建，而是直接向Spring要==，这种思想就是==IOC==



##IOC思想介绍(重点)

> IOC(  控制  反转  )是一种设计思想,  它的目的是指导我们设计出更加松耦合的程序。(解耦)
>
> 控制：指的是控制权，现在可以简单理解为对象的创建权限
>
> 反转：指的对象的控制权由程序员在==类中主动控制(new)==反转到由==Spring容器来控制==。

找对象

​	传统方式 : 自己找

​	婚介所    :  先将自己的信息注册到婚介所, 然后到你需要对象的时候, 直接问婚介所要一个对象, 婚介所会给你想办法创建一个对象出来,送过来



new-----要