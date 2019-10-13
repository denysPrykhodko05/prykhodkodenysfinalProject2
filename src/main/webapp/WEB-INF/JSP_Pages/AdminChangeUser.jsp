<%--
  Created by IntelliJ IDEA.
  User: user1
  Date: 09.10.2019
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<c:forEach var="user" items="${users}">
    <ul>

        <li>Name: <c:out value="${user.login}"/></li>

        <li>Role: <c:out value="${user.role}"/></li>
    </ul>
    <hr />

</c:forEach>
<form method="post">
    Login: <input type="text" name="login"><br>
    Password: <input type="text" name="password"><br>
    <input type="radio" name="role" value="USER">User<br>
    <input type="radio" name="role" value="ADMIN">Admin<br>
    <input type="submit" name="apply" value="Ok">
</form>
</body>
</html>
