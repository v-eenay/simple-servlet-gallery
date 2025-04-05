<%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/5/2025
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form action="/login" method="post">
        <fieldset>
            <legend>Login</legend>
            <label>Email: <input type="text" name="email" /></label>
            <br>
            <label>Password: <input type="password" name="password" /></label>
            <br>
            <input type="submit" value="Login" />
        </fieldset>
    </form>
    <a href="${pageContext.request.contextPath}/register">Register</a>
</body>
</html>
