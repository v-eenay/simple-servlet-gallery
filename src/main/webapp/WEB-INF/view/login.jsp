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
    <title>Login - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <h1>Image Gallery</h1>
        <h2>Sign in to continue</h2>
        
        <%
            String regerror = request.getParameter("regerror");
            if (regerror != null && regerror.equals("false")) {
        %>
            <div class="message success">Registration successful. Please sign in.</div>
        <%
            }
            String error = request.getParameter("error");
            if (error != null && error.equals("true")) {
        %>
            <div class="message error">Invalid email or password.</div>
        <%
            }
        %>
        
        <form action="${pageContext.request.contextPath}/login" method="post">
            <fieldset>
                <legend>Login</legend>
                <label>
                    Email Address
                    <input type="email" name="email" required placeholder="Enter email" />
                </label>
                <label>
                    Password
                    <input type="password" name="password" required placeholder="Enter password" />
                </label>
                <input type="submit" value="Sign In" />
            </fieldset>
        </form>
        
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/register" class="button secondary">Create Account</a>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
