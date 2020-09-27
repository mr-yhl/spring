[pom.xml](./pom.xml) | [controller](./src/main/java/cn/com/mryhl/controller) | [domain](./src/main/java/cn/com/mryhl/domain) |
[handlers](./src/main/java/cn/com/mryhl/handlers) | [spring-mvc.xml](./src/main/resources/spring-mvc.xml) | 
[页面资源](./src/main/webapp)
### 使用拦截器完成用户的访问拦截(重要)
#### 5.1 思路分析

> 用户访问一个主页面index.jsp(首页, 权限控制)
>
> ​	如果用户已经登录，即可成功访问
>
> ​	如果用户没有登录，不允许访问，返回登录页面login.jsp


#### 5.2 复制工程

```xml
<module>day05_mvc_login</module>
```

#### 5.3 login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  User: mr_yhl
  Date: 2020/9/27
  Time: 20:29  
--%>
<html>
    <head>
        <title>Login登陆</title>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/userController/login" method="post">
            用户姓名:<input type="text" name="username"><br>
            安全密码:<input type="text" name="password"><br>
            <input type="submit" value="提交">
        </form>

    </body>
</html>
```

 

#### 5.4 配置欢迎页

```xml
<!--配置欢迎页面-->
<welcome-file-list>
    <welcome-file>login.jsp</welcome-file>
</welcome-file-list>
```

####  5.5 UserController

```java
@Controller
public class UserController {
    /**
     * 登录
     * @return
     */
    @RequestMapping("/userController/login")
    public String login(String username, String password, HttpSession session) {
        if ("admin".equals(username)){
            // 记录session
            session.setAttribute("loginname",username);

            // 跳转到主页面
            return "redirect:/userController/toIndex";
        }else {
            //登录失败
            return "redirect:/login.jsp";
        }

    }


    /**
     * 跳转到主页
     * @return
     */
    @RequestMapping("/userController/toIndex")
    public String toIndex(){
        return "index";
    }
}
```

#### 5.6 index.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        你好,少年郎...

    </body>
</html>
```

 

#### 5.7 开发权限拦截器

```java
public class MyHandlerInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String loginname = (String) request.getSession().getAttribute("loginname");
        
        if (loginname != null){
            return true;
        }else {
        // 返回登录页面
        response.sendRedirect(request.getContextPath()+"/login.jsp");    
        
        return false;
        }
    }
}
```

#### 5.8配置权限拦截器

```xml
<!--配置拦截器-->
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/**"/>
        <mvc:exclude-mapping path="/userController/login"/>
        <bean class="cn.com.mryhl.handlers.MyHandlerInterceptor"></bean>
    </mvc:interceptor>
</mvc:interceptors>
```

 