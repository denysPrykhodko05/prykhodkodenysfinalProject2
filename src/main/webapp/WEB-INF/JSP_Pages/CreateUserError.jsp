<%--
  Created by IntelliJ IDEA.
  User: user1
  Date: 09.10.2019
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create User</title>
</head>
<body>

<c:forEach var="user" items="${requestScope.users}">
    <ul>

        <li>Имя: <c:out value="${user.name}"/></li>

        <li>Возраст: <c:out value="${user.age}"/></li>
    </ul>
    <hr />

</c:forEach>
<c:out value="${requestScope.error}"/><br>
<form method="post" action="">
    Login: <input type="text" name="login"><br>
    Password: <input type="text" name="password"><br>
    <input type="radio" name="role" value="User">User<br>
    <input type="radio" name="role" value="Admin">Admin<br>
    <input type="submit" name="apply" value="Ok">
</form>
</body>
</html>
