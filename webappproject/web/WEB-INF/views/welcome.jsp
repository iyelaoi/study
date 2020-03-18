<%--
  Created by IntelliJ IDEA.
  User: wqz
  Date: 19-9-30
  Time: 下午2:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="cn.wqz.model.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <title>Insert title here</title>
</head>
<body>
<%
    Account account = (Account)session.getAttribute("account");
%>
<h2>
    Welcome to "<%= account.getUsername()%>" Login System!
</h2>
This is the first javaWeb Project!
</body>
</html>
