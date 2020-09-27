<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Title</title>
        <%--引入jq--%>
        <script src="${pageContext.request.contextPath}/js/jquery-3.3.1.js"></script>
    </head>
    <body>
        <%--页面跳转之转发--%>
        <a href="${pageContext.request.contextPath}/demoController/demo1">
            转发--简单方式
        </a><br>

        <a href="${pageContext.request.contextPath}/demoController/demo2">
            转发--forward方式
        </a><br>

        <a href="${pageContext.request.contextPath}/demoController/demo3">
            转发--原生api方式
        </a><br>

        <a href="${pageContext.request.contextPath}/demoController/demo111">
            转发--传递参数
        </a><br>

        <%--页面跳转之重定向--%>
        <a href="${pageContext.request.contextPath}/demoController/demo4">
            重定向--redirect
        </a><br>
        <a href="${pageContext.request.contextPath}/demoController/demo5">
            重定向--原生api方式
        </a><br>
        <a href="${pageContext.request.contextPath}/demoController/demo6">
            通过重定向访问WEB-INF下资源(经过了一次转发)
        </a><br>
        <%--Ajax+json实现异步交互--%>
        <button id="ajax-json">Ajax+json实现异步交互</button>
        <br>

        <script>
            $("#ajax-json").click(function () {
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/demoController/demo7",
                    contentType: "application/json",/*代表是前台发送到后台的数据类型是json必须写*/
                    dataType: "json",/*要求后台返回的数据类型*/
                    data: '[{"name":"丁凤","age":18},{"name":"北山勃","age":19}]',
                    success: function (data) {
                        for (let e of data) {
                            alert(e.name);
                        }
                    }
                });
            });
        </script>

        <%--restful风格--%>
        <button id="restful">restful风格测试</button>
        <br>
        <script>
            $("#restful").click(function () {
                $.ajax({
                    type: "POST",
                    url: "${pageContext.request.contextPath}/user",
                    contentType: "application/json",/*代表是前台发送到后台的数据类型是json必须写*/
                    dataType: "json",/*要求后台返回的数据类型*/
                    data: '{"name":"丁凤","age":18}',
                    success: function (data) {
                        alert("提交成功...");
                    }
                });
            });
        </script>
        <%--restful风格--%>
        <button id="restful-get">restful风格测试</button>
        <br>
        <script>
            $("#restful-get").click(function () {
                $.ajax({
                    type: "GET",
                    url: "${pageContext.request.contextPath}/user/id/1/name/zs",
                    contentType: "application/json",/*代表是前台发送到后台的数据类型是json必须写*/
                    dataType: "text",/*要求后台返回的数据类型*/
                    data: '',
                    success: function (data) {
                        alert("提交成功...");
                    }
                });
            });
        </script>

        <a href="${pageContext.request.contextPath}/demoController/demo9">
            拦截模拟
        </a><br>


    </body>
</html>
