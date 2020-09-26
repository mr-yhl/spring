[pom.xml](./pom.xml) | [SpringMVC配置文件](./src/main/resources/spring-mvc.xml) | [web.xml](./src/main/webapp/WEB-INF/web.xml) |
[主页](./src/main/webapp/index.jsp) | [实体](./src/main/java/cn/com/mryhl/domain) | [控制器](./src/main/java/cn/com/mryhl/controller/DemoController.java) |
[类型转换器](./src/main/java/cn/com/mryhl/converters/DateConverter.java)
## 今日内容

+ SpringMVC认识
+ SpringMVC原理
+ 接收请求参数
+ 参数处理
+ 接收请求头

## 第一章 SpringMVC

### 1. MVC模式(了解)

MVC是一种用于设计创建 Web 应用程序表现层的模式,使用它可以将业务逻辑、数据、界面显示代码分离开来.

- Model（模型）: 指的就是数据模型,用于封装数据
- View（视图）:	用于数据展示
- Controller（控制器）: 用于程序处理逻辑

> web层职责

1. 接收请求
2. 调用service
3. 返回页面
4. 返回数据
5. 全局异常处理

###  2. SpringMVC介绍(了解)

> SpringMVC是Spring产品对MVC模式的一种具体实现，**它可以通过一套注解，让一个简单Java类成为控制器**。

SpringMVC将Servlet分离为两个部分,分别是负责请求转发的DispatcherServlet和负责调用的JavaBean 

## 第二章 SpringMVC入门案例(重点)

> 开发一个请求页面, 向后台发送一个请求, 后台需要在控制台打印一句话,  然后跳转到一个新的返回页面

### 1. 创建一个新工程, 导入坐标

```xml
<dependencies>
  <!--springMVC-->
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.1.6.RELEASE</version>
  </dependency>
  <!--servlet-->
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.5</version>
  </dependency>
  <!--jsp-->
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jsp-api</artifactId>
    <version>2.0</version>
  </dependency>
</dependencies>
```

### 2.提供一个springmvc的配置文件

> 组件扫描  引入三大组件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!--组件扫描-->
    <context:component-scan base-package="cn.com.mryhl.controller"></context:component-scan>
    <!--三大组件-->
    <!--注解驱动:相当于增强版的处理器适配器和处理器映射器-->
    <mvc:annotation-driven />
    <!--视图解析器-->
    <bean id="viewResolver"
          class="org.springframework.web.servlet.view.InternalResourceViewResolver"></bean>
</beans>
```

 

### 3. 在web.xml中配置前端控制器

```xml
<!--前端控制器-->
<servlet>
   <servlet-name>dispatcherServlet</servlet-name>
   <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
   <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring-mvc.xml</param-value>
   </init-param>
</servlet>
<servlet-mapping>
   <servlet-name>dispatcherServlet</servlet-name>
   <!--/ 默认配置,拦截的是除了.jsp之外的所有路径-->
   <url-pattern>/</url-pattern>
</servlet-mapping>
```

 

### 4. 开发请求页面

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  User: mr_yhl
  Date: 2020/9/26
  Time: 9:48  
--%>
<html>
    <head>
        <title>SpringMVC请求页面</title>
    </head>
    <body>
        <a href="${pageContext.request.contextPath}/demoController/demo1">demo1入门案例</a>
    </body>
</html>
```

 

### 5. 开发一个控制器

```java
/**
 * @Controller将当前类放入SpringMVC容器中
 */
@Controller
public class DemoController {
    /**
     * 编写控制器
     * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配
     */
    @RequestMapping("/demoController/demo1")
    public String demo01(){
        System.out.println("后台执行了...");
        // 返回值就是下一步要转发的页面
        return "/WEB-INF/success.jsp";
    }
}
```

###  6.开发响应页面

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  User: mr_yhl
  Date: 2020/9/26
  Time: 11:58  
--%>
<html>
    <head>
        <title>成功</title>
    </head>
    <body>
        跳转成功...
    </body>
</html>
```

 

### 7. 部署测试

> 页面正常跳转到指定页面,控制台打印出指定内容

### 8. 常用的注解

> @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配
>
> @Controller将当前类放入SpringMVC容器中

 

## 第三章 SpringMVC原理(面试必问)



### 1. SpringMVC的工作流程


1. 用户通过浏览器发送请求至**DispatcherServlet**
2. DispatcherServlet收到请求调用**HandlerMapping**
3. HandlerMapping找到具体的处理器链返回给DispatcherServlet
4. DispatcherServlet会根据返回的处理器链调用**HandlerAdapter**
5. HandlerAdapter经过适配调用具体的**Handler**（controller）
6. Controller执行完成返回一个执行结果
7. HandlerAdapter将Handler的结果ModelAndView对象返回给DispatcherServlet
8. DispatcherServlet将ModelAndView对象传给**ViewReslover**
9. ViewReslover解析后得到具体View，并返回给DispatcherServlet
10. DispatcherServlet根据View进行视图渲染（即将模型数据填充至视图中）
11. DispatcherServlet会将渲染后的视图响应给浏览器

### 2. SpringMVC的三大组件

处理器映射器   负责根据URL寻找对应的处理器方法

处理器适配器   负责真正的去调用某个处理器方法

视图解析器      负责将逻辑视图转换成物理视图

>  SpringMVC自带三大组件,如果不进行配置也能正常使用.配置的相当于增强版

```xml
<!--视图解析器-->
<bean id="viewResolver"
      class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <!--SpringMVC的前端控制器允许我们以配置的形式传入前缀和后缀,他会在跳转视图的时候自动拼接-->
    <property name="prefix" value="/WEB-INF/"></property>
    <property name="suffix" value=".jsp"></property>
</bean>
```

###  3.RequestMapping(会用)

RequestMapping用于建立请求URL和处理方法之间的对应关系，也可以通过它的属性对请求做出各种限制

- value：  用于限制请求URL(和path作用一样)

- method：用于限制请求类型

- params：用于限制请求参数的条件

**此注解可以标注在方法上，也可以标注在类上，标注在类上代表类中的所有方法都可以共用一段URL**

```java
@Controller
@RequestMapping("/demoController")
public class DemoController {
    /**
     * 编写控制器
     * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配
     */
    @RequestMapping("/demo1")
    public String demo01(){
        System.out.println("后台执行了...");
        // 返回值就是下一步要转发的页面
        return "success";
    }
}
```

 

### 4. 限制请求路径

#### 4.1 页面

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo2">demo2@RequestMapping-path</a>
<a href="${pageContext.request.contextPath}/demoController/demo3">demo2@RequestMapping-path</a>

```

#### 4.2 后台

```java
@RequestMapping(path = {"/demo2","/demo3"})
public String demo02(){
    System.out.println("后台执行了...");
    // 返回值就是下一步要转发的页面
    return "success";
}
```

 

### 5. 限制请求类型

> method,作用就是用于限定当前方法的提交方式,支持数组写法(同时多个)
> 		如果不写method,代表所有请求方式都能运行

#### 5.1 页面

```jsp
<form action="${pageContext.request.contextPath}/demoController/demo4" method="post">
    <input type="submit" value="demo4@RequestMapping-mothod">
</form>
```

 

#### 5.2 后台

```java
/**
 * 编写控制器
 * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配
 * method,作用就是用于限定当前方法的提交方式,支持数组写法(同时多个)
 * 如果不写method,代表所有请求方式都能运行
 */
@RequestMapping(value = "/demo4",method = RequestMethod.POST)
public String demo04(){
    System.out.println("value = \"/demo3\",method = RequestMethod.POST");
    // 返回值就是下一步要转发的页面
    return "success";
}
```

  

### 6. 限制请求参数

> params用于限定请求参数的必传，不写代表不限制

#### 6.1 页面

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo5?username=11">demo5@RequestMapping-params</a><br>

```

 

#### 6.2 后台

```java
/**
 * 编写控制器
 * @RequestMapping:用于为当前方法绑定一个URL地址,作用就是为了前端请求的匹配
 * params用于限定请求参数的必传，不写代表不限制
 */
@RequestMapping(value = "/demo5",params = "username")
public String demo05(){
    System.out.println("username");
    // 返回值就是下一步要转发的页面
    return "success";
}
```

 



## 第四章 接收请求参数(重点)

在SpringMVC中可以使用多种类型来接收前端传入的参数(字符串)

### 1. 简单类型

只需要保证前端传递的参数名称跟方法的形参名称一致就好

#### 1.1页面

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo6?username=张三&age=11">demo6接收参数--简单类型</a><br>
```

####  1.2后台

```java
/**
 * 编写控制器,接收简单参数
 * 简单类型参数接收:需要保证前端传递的参数名称跟方法的形参名称一致就好
 * 对于简单类型的数据,SpringMVC底层内置了类型转换器
 */
@RequestMapping(value = "/demo6")
public String demo06(String username,Integer age){
    System.out.println("username = "+username+", age = " + age);
    // 返回值就是下一步要转发的页面
    return "success";
}
// username = 张三, age = 11
```

 

### 2. 对象类型

只需要保证前端传递的参数名称跟pojo的属性名称（set方法）一致就好

#### 2.1 页面

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo7?username=张三&age=11">demo7接收参数--对象类型</a><br>
```

####  2.2 封装一个实体

```java
public class User {
    private String username;
    private Integer age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
```

 

#### 2.3 后台

```java
/**
 * 编写控制器,接收对象类型
 * 接收对象类型：需要保证前端传递的参数名称跟pojo的属性名称（set方法）一致就好
 */
@RequestMapping(value = "/demo7")
public String demo07(User user){
    System.out.println(user);
    // 返回值就是下一步要转发的页面
    return "success";
}
```

 

### 3.数组类型

>  只需要保证前端传递的参数名称跟方法中的数组形参名称一致就好

#### 3.1 页面

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo8?students=张三&students=李四">demo8接收参数--数组类型</a><br>
```

#### 3.2后台

```java
/**
 * 如果前端传入的是数组,但是后台以字符串接收, Spring会将参数以,分隔拼接成一个串传进来[ public String demo8(String students)]
 * 如果前端传入的是数组,后台也是以数组接收, 只需要保证前端传递的参数名称跟方法中的数组形参名称一致就好
 */
@RequestMapping(value = "/demo8")
public String demo08(String[] students){
    for (String student : students) {
        System.out.println(student);
    }
    // 返回值就是下一步要转发的页面
    return "success";
}
```

###  4. 集合类型

> 获取集合参数时，要将集合参数包装到一个pojo中才可以

#### 4.1页面

```jsp
<form action="${pageContext.request.contextPath}/demoController/demo9" method="post">
    第1个User的username:<input type="text" name="users[0].username" value="张三"><br>
    第1个User的age:<input type="text" name="users[0].age" value="16"><br>
    第2个User的username:<input type="text" name="users[1].username" value="李四"><br>
    第2个User的age:<input type="text" name="users[1].age" value="15"><br>

    map的第一个元素:<input type="text" name="map['1001']" value="zs">          

    map的第二个元素:<input type="text" name="map['1002']" value="ls">           

    <input type="submit" value="接收参数--集合类型"><br>           


</form>
```

#### 4.2封装一个Vo对象

```java
public class Vo {
    private List<User> users = new ArrayList<User>();
    private Map<String,String> map = new HashMap<String, String>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Vo{" +
                "users=" + users +
                ", map=" + map +
                '}';
    }
}
```

#### 4.3 后台

```java
/**
 *获取集合参数时，要将集合参数包装到一个pojo中才可以
 */
@RequestMapping(value = "/demo9")
public String demo09(Vo vo){
    System.out.println(vo);
    // 返回值就是下一步要转发的页面
    return "success";
}
```

###  5.日期类型(面试)

> 对于一些常见的类型,  SpringMVC是内置了类型转换器的,  但是对于一些格式比较灵活的参数(日期  时间), SpringMVC无法完成类型转换
>
> 这时候就必须**自定义类型转换器**
>
> **其实SpringMVC也内置日期类型的转换器,  格式: yyyy/MM/dd**

#### 5.1 页面

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo10?myDate=2020-09-26">demo10接收参数--日期类型</a><br>
```

 

#### 5.2 自定义时间类型转换器

1) 自定义一个类型转换器类,实现类型转换的方法

```java
/**
 * 自定义一个类型转换器，实现类型转换方法（必须实现Converter<原始类型,目标类型>接口）
 */
public class DateConverter implements Converter<String,Date> {

    public Date convert(String s){
        Date date = null;
        try{
           date= new SimpleDateFormat("yyyy-MM-dd").parse(s);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;

    }


}

```

 

2) 将转换器的类注册到转换服务,并且将转换服务注册到Springmvc的注册驱动中

```xml
<!--注解驱动:相当于增强版的处理器适配器和处理器映射器-->
<mvc:annotation-driven conversion-service="conversionService2" />
<bean id="conversionService2" class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean class="cn.com.mryhl.converters.DateConverter"></bean>
        </set>
    </property>
</bean>
```

####  5.3 后台

```java
/**
 *获取日期类型参数
 */
@RequestMapping(value = "/demo10")
public String demo10(Date myDate){
    System.out.println(myDate);
    // 返回值就是下一步要转发的页面
    return "success";
}
```

 

### 6. 文件类型(文件上传)

#### 6.1 加入文件上传的包

```xml
<!--文件上传-->
<dependency>
  <groupId>commons-fileupload</groupId>
  <artifactId>commons-fileupload</artifactId>
  <version>1.4</version>
</dependency>
```

 

#### 6.2配置文件上传解析器

```xml
<!--
文件上传解析器
注意:此处的id是固定值
-->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <!--限制每此上传文件的大小,单位是B-->
    <property name="maxUploadSize" value="5242880"></property>
</bean>
```

 

#### 6.3 单文件上传

##### 6.3.1 页面

```jsp
<%--
文件上传三要素:
1) method = "post"
2) enctype = "multipart/form-date"
3) 必须有一个type = file的input域
--%>
<form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/demoController/demo11">
    <input type="file" name="uploadFile">
    <input type="submit" value="单文件上传">
</form>
```

##### 6.3.2后台

```java
/**
 *文件上传
 * 参数类型必须是MultipartFile,参数名必须域前台一致
 */
@RequestMapping(value = "/demo11")
public String demo11(MultipartFile uploadFile) throws IOException {
    // 生产新的文件名
    String newFileName = UUID.randomUUID().toString() + uploadFile.getOriginalFilename();
    // 定义一个本地目录
    File newFile = new File(new File("F:\\code\\spring\\day04_MVC"), newFileName);

    uploadFile.transferTo(newFile);

    return "success";
}
```

 

#### 6.4 多文件上传

##### 6.4.1页面

```jsp
<form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/demoController/demo12">
    <input type="file" name="uploadFiles" multiple>
    <input type="submit" value="多文件上传">
</form>
```

 

##### 6.4.2 后台

```java
/**
 *文件上传
 * 参数类型必须是MultipartFile,参数名必须域前台一致
 */
@RequestMapping(value = "/demo12")
public String demo12(MultipartFile[] uploadFiles) throws IOException {
    for (MultipartFile uploadFile : uploadFiles) {
        // 生产新的文件名
        String newFileName = UUID.randomUUID().toString() + uploadFile.getOriginalFilename();
        // 定义一个本地目录
        File newFile = new File(new File("F:\\code\\spring\\day04_MVC"), newFileName);

        uploadFile.transferTo(newFile);
    }

    return "success";
}
```

 

## 第五章 接收参数的处理

### 5.1 中文乱码

> SpringMVC在使用post提交请求时,  对于中文参数是有乱码问题的, 针对这种情况它提供了一个中文乱码过滤器,  我们只需要进行配置一下就可以了.

```xml
<!--中文乱码过滤器-->
   <filter>
       <filter-name>characterEncodingFilter</filter-name>
       <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
       <init-param>
           <param-name>encoding</param-name>
           <param-value>UTF-8</param-value>
       </init-param>
   </filter>
   <filter-mapping>
       <filter-name>characterEncodingFilter</filter-name>
       <url-pattern>/*</url-pattern>
   </filter-mapping>
```

### 5.2 @RequestParam

@RequestParam标注在方法参数之前，用于对传入的参数做一些限制，支持三个属性:

- value：默认属性，用于指定前端传入的参数名称
- required：用于指定此参数是否必传
- defaultValue：当参数为非必传参数且前端没有传入参数时，指定一个默认值

#### 5.2.1 前端

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo13?students=张三&students=李四">demo13接收参数--@RequestParam</a><br>
```

 

#### 5.2.2后台

```java
/**
 * @RequestParam 标注在方法参数之前, 用于表示当前参数是获取的前端传递过来的哪个参数的值
 * @RequestParam 标注在方法参数之前, 此参数就是必传选项,不传就会报错 , 但是可以使用required=false来取消这个限制
 * defaultValue  可以为当前参数设置一个默认值, 当前端不再传递此参数的时候,就使用默认值
 * @RequestParam(value = "students")  可以接收一个集合参数,直接封装到一个集合对象中去
 */
@RequestMapping(value = "/demo13")
public String demo13(
        @RequestParam(value = "page_size", required = false, defaultValue = "20") String pageSize,
        @RequestParam(value = "students") List<String> students
) {
    System.out.println(pageSize);
    for (String student : students) {
        System.out.println(student);

    }

    return "success";
}
```

 

## 第六章 接收请求头信息(了解)

### 1. 前端

```jsp
<a href="${pageContext.request.contextPath}/demoController/demo14">demo14接收请求头参数</a><br>
```

 

### 2. 后台

```java
/**
 * 接收请求头
 * @RequestHeader  用于接收请求头中的所有信息, 会封装到一个Map结构中去
 * @RequestHeader(key)  用于接收请求头中的某一项信息
 * @CookieValue(key)  用于接收cookie中的某一项信息
 */
@RequestMapping(value = "/demo14")
public String demo14(
        @RequestHeader Map map,
        @RequestHeader("cookie") String cookie,
        @CookieValue("JSESSIONID") String jsessionid
) {
    System.out.println(map);
    System.out.println(cookie);
    System.out.println(jsessionid);
    return "success";
}
```

> 运行结果
>
> {host=localhost:8080, connection=keep-alive, upgrade-insecure-requests=1, user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36, accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9, sec-fetch-site=same-origin, sec-fetch-mode=navigate, sec-fetch-user=?1, sec-fetch-dest=document, referer=http://localhost:8080/, accept-encoding=gzip, deflate, br, accept-language=zh-CN,zh;q=0.9, cookie=Idea-affb8a85=347d378c-a966-4e19-a8bb-71aac3e665bb; JSESSIONID=372DA54978F9BF7DC7ED8CF9DD78F197}
> Idea-affb8a85=347d378c-a966-4e19-a8bb-71aac3e665bb; JSESSIONID=372DA54978F9BF7DC7ED8CF9DD78F197
> 372DA54978F9BF7DC7ED8CF9DD78F197

 @RequestParam

@RequestParam标注在方法参数之前，用于对传入的参数做一些限制,支持三个属性:

- value：默认属性，用于指定前端传入的参数名称
- required：用于指定此参数是否必传
- defaultValue：当参数为非必传参数且前端没有传入参数时，指定一个默认值

 