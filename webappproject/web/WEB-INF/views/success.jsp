<%--
  Created by IntelliJ IDEA.
  User: wqz
  Date: 19-9-30
  Time: 下午3:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="cn.wqz.model.*" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        Account account = (Account)session.getAttribute("account");
    %>
    username:<%= account.getUsername()%>
    <br>
    password:<%= account.getPassword() %>

</body>
</html>
