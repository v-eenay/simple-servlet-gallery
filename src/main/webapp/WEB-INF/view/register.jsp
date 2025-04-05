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
    <title>Title</title>
</head>
<body>
    <h1>Register</h1>
    <form action="" method="post">
        <fieldset>
            <legend>Register</legend>
            <label for="fullname">Full Name:<input type="text" id="fullname" name="fullname"></label>
            <br><label for="email">Email Address:<input type="text" id="email" name="email"></label>
            <br><label for="password">Password:<input type="password" id="password" name="password"></label>
            <br><input type="submit" value="Register">
        </fieldset>
    </form>
    <a href="${pageContext.request.contextPath}">Login</a>
</body>
</html>
