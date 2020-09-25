
[pow.xml](./pom.xml) | [applicationContext.xml](./src/main/resources/applicationContext.xml) | 
[log文件](./src/main/java/cn/com/mryhl/log/Logger.java) | [service层](./src/main/java/cn/com/mryhl/service) |
  [测试文件](./src/test/java/cn/com/mryhl/test/AccountServiceTest.java)
##  SpringAOP的入门案例(重点)

> 需求: 在AccountServiceImpl类中的方法上打印日志

### 1. 思路分析

```markdown
找二配一:
	1. 目标对象
	2. 增强对象
	3. 配置切面----->生产代理对象
```

### 2. 代码开发

#### 2.1 创建一个新工程

> 引入两个坐标
>
> 1. spring坐标
> 2. 切入点表达式坐标

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

    <artifactId>day03_03_aop</artifactId>
    <dependencies>
        <!--spring-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.6.RELEASE</version>
        </dependency>
        <!--切入点表达式坐标-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.7</version>
        </dependency>
    </dependencies>

</project>
```

####  2.2 创建目标类(AccountServiceImpl)

> 接口

```java
/**
 * 创建service接口
 */
public interface AccountService {
    /**
     * 插入内容方法
     */
    void save(Object o);
    /**
     * 查询所有
     */
    List<Object> findAll();
    /**
     * 通过姓名查找
     */
    Object findByName(String name);
    
}
```

> 实现类

```java
/**
 * 实现类
 */
public class AccountServiceImpl implements AccountService {
    public void save(Object o) {
        System.out.println("save");

    }

    public List<Object> findAll() {
        System.out.println("findAll");
        return null;
    }

    public Object findByName(String name) {
        System.out.println("findByName");
        return null;
    }
}
```

####  2.3创建增强类(Logger)

```java
/**
 * 增强方法,打印日志
 */
public class Logger {
    /**
     * 被增强方法执行前
     */
    public void beforeMethod(){
        System.out.println(new Date()+"方法执行了..");
    }
}
```

####  2.4配置切面(重点)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">
    <!--目标对象-->
    <bean id="accountService" class="cn.com.mryhl.service.impl.AccountServiceImpl"></bean>
    <!--增强对象-->
    <bean id="logger" class="cn.com.mryhl.log.Logger"></bean>
    <!--aop配置-->
    <aop:config>
        <!--配置切点-->
        <aop:pointcut id="pt" expression="execution(* cn.com.mryhl.service.impl.*.*(..))"/>
        <!--配置切面-->
        <!--切面: 切点(目标对象) + 增强(增强对象)
            切面是一种描述, 描述了这样一件事: 一个  [什么样的增强功能]   添加在了    [哪些切点的]    [什么位置]上
           切面就是描述的切点方法和增强方法的执行顺序

           aspect ref="logger"  用于指定增强类   method  用于指定增强类中的一个具体增强方法
           pointcut-ref         用于指定切点
           before               增强方法在切点方法的指点运行-->
        <aop:aspect ref="logger">
            <aop:before method="beforeMethod" pointcut-ref="pt"></aop:before>
        </aop:aspect>
    </aop:config>
</beans>
```



#### 2.5测试

```java
public class AccountServiceTest {
    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContest.xml");

        AccountService accountService = act.getBean(AccountService.class);
        accountService.findAll();
    }
}
```



##  SpringAOP配置详解(重点)

### 1. 切点表达式

切点表达式的作用:  定义一组规则, 用于在连接点中挑选切点

```xml
<!--*  占位符,表示一个或多个
.. 占位符,表示零个或多个-->
<aop:pointcut id="pt" expression="execution(* cn.com.mryhl.service.impl.*.*(..))"/>
```

###  2. 四大通知

四大通知描述的就是增强方法在切点方法的什么位置上执行

- 前置通知(before) ：在切点运行之前执行
- 后置通知(after-returning)：在切点正常运行结束之后执行
- 异常通知(after-throwing)：在切点发生异常的时候执行
- 最终通知(after)：在切点的最终执行

```java
        try {
            //前置通知(before) ：在切点运行之前执行
            
            //切点执行
            
            //后置通知(after-returning)：在切点正常运行结束之后执行
            
        }catch (Exception e){
            //异常通知(after-throwing)：在切点发生异常的时候执行
            
        }finally {
            //最终通知(after)：在切点的最终执行
        }
```

#### 2.1添加方法

```java
/**
 * 增强方法,打印日志
 */
public class Logger {
    /**
     * 被增强方法执行前
     */
    public void beforeMethod(){
        System.out.println(new Date()+"方法开始执行了..");
    }

    /**
     * 被增强方法正常执行完成
     */
    public void afterReturnMethod(){
        System.out.println(new Date()+"方法执行完成了..");
    }

    /**
     * 被增强方法出现异常
     */
    public void afterThrowMethod(){
        System.out.println(new Date()+"方法出现异常了..");
    }
    /**
     * 被增强方法到最后
     */
    public void afterMethod(){
        System.out.println(new Date()+"方法执行到最后..");
    }
}
```

#### 2.2添加配置

```xml
<aop:aspect ref="logger">
    <aop:before method="beforeMethod" pointcut-ref="pt"></aop:before>
    <aop:after-returning method="afterReturnMethod" pointcut-ref="pt"></aop:after-returning>
    <aop:after-throwing method="afterThrowMethod" pointcut-ref="pt"></aop:after-throwing>
    <aop:after method="afterMethod" pointcut-ref="pt"></aop:after>
</aop:aspect>
```

 

**当四大通知同时出现的时候, 它的执行顺序会受到配置顺序的影响**

### 3. 环绕通知

它是一种特殊的通知，他允许你以编码的形式实现四大通知

#### 3.1编码

> 传入参数 ProceedingJoinPoint pjp
>
> 切点执行
>
> pjp.proceed();

```java
/**
 * 环绕通知
 */
public void aroundMethod(ProceedingJoinPoint pjp){
    try {
        System.out.println("即将执行方法.......");

        //切点执行
        pjp.proceed();

        System.out.println("方法正常结束.......");

    }catch (Throwable e){
        e.printStackTrace();
        System.out.println("方法出现异常.......");

    }finally {
        System.out.println("方法运行到最后.......");
    }
}
```

 

#### 3.2配置

```xml
<aop:aspect ref="logger">
    <!--<aop:before method="beforeMethod" pointcut-ref="pt"></aop:before>
    <aop:after-returning method="afterReturnMethod" pointcut-ref="pt"></aop:after-returning>
    <aop:after-throwing method="afterThrowMethod" pointcut-ref="pt"></aop:after-throwing>
    <aop:after method="afterMethod" pointcut-ref="pt"></aop:after>-->
    <aop:around method="aroundMethod" pointcut-ref="pt"></aop:around>
</aop:aspect>
```

 



##  AOP工作原理(面试)

### 1. 开发阶段(开发者完成)

开发共性功能，制作成增强

开发非共性功能，制作成切点

在配置文件中，声明切点与增强间的关系，即切面

### 2. 容器启动阶段(AOP完成)

Spring读取配置文件中的切面信息，根据切面中的描述, 将**增强功能**增加在**目标对象的切点方法**上，动态创建代理对象, 最后将代理对象放入容器中 

>面试题:
>
>1. 正向面试 : 1) 日志处理  2) 事务   3) 性能统计
>2. 反向面试：

