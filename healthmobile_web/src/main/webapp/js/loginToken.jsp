<%--
  User: Wang
  Date: 2019/11/29
  Time: 21:31
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<script>
    axios.interceptors.request.use(function (config) {
        <%--添加请求拦截器--%>
        <%--// 在发送请求之前将登录保存的token添加在请求头里面--%>
        config.headers.token = localStorage.getItem("token");
        return config;
    }, function (error) {
        <%--// 对请求错误做些什么--%>
        return Promise.reject(error);
    });
</script>
</body>
</html>
