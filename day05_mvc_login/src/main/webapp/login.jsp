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
