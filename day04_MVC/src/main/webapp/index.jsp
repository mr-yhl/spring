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
        <a href="${pageContext.request.contextPath}/demoController/demo1">demo1入门案例</a><br>
        <a href="${pageContext.request.contextPath}/demoController/demo2">demo2@RequestMapping-path</a><br>
        <a href="${pageContext.request.contextPath}/demoController/demo3">demo2@RequestMapping-path</a><br>

        <form action="${pageContext.request.contextPath}/demoController/demo4" method="post">
            <input type="submit" value="demo4@RequestMapping-mothod"><br>
        </form>

        <a href="${pageContext.request.contextPath}/demoController/demo5?username=11">demo5@RequestMapping-params</a><br>
        <a href="${pageContext.request.contextPath}/demoController/demo6?username=张三&age=11">demo6接收参数--简单类型</a><br>
        <a href="${pageContext.request.contextPath}/demoController/demo7?username=张三&age=11">demo7接收参数--对象类型</a><br>

        <a href="${pageContext.request.contextPath}/demoController/demo8?students=张三&students=李四">demo8接收参数--数组类型</a><br>

        <form action="${pageContext.request.contextPath}/demoController/demo9" method="post">
            第1个User的username:<input type="text" name="users[0].username" value="张三"><br>
            第1个User的age:<input type="text" name="users[0].age" value="16"><br>
            第2个User的username:<input type="text" name="users[1].username" value="李四"><br>
            第2个User的age:<input type="text" name="users[1].age" value="15"><br>
            map的第一个元素:<input type="text" name="map['1001']" value="zs"><br>
            map的第二个元素:<input type="text" name="map['1002']" value="ls"><br>
            <input type="submit" value="接收参数--集合类型"><br>
        </form>
        <a href="${pageContext.request.contextPath}/demoController/demo10?myDate=2020-09-26">demo10接收参数--日期类型</a><br>
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

        <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/demoController/demo12">
            <input type="file" name="uploadFiles" multiple>
            <input type="submit" value="多文件上传">
        </form>

        <a href="${pageContext.request.contextPath}/demoController/demo13?students=张三&students=李四">demo13接收参数--@RequestParam</a><br>
        <a href="${pageContext.request.contextPath}/demoController/demo14">demo14接收请求头参数</a><br>
    </body>
</html>
