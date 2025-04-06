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
    <title>Register - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <h1>Image Gallery</h1>
        <h2>Create a new account</h2>
        
        <%
            String error = request.getParameter("regerror");
            if (error != null && error.equals("true")) {
        %>
            <div class="message error">Registration failed. Email may be in use.</div>
        <%
            }
        %>
        
        <form action="${pageContext.request.contextPath}/register" method="post">
            <fieldset>
                <legend>Register</legend>
                <label>
                    Full Name
                    <input type="text" name="fullname" required placeholder="Enter your name" />
                </label>
                <label>
                    Email Address
                    <input type="email" name="email" required placeholder="Enter email" />
                </label>
                <label>
                    Password
                    <input type="password" name="password" required placeholder="Create password" />
                </label>
                <input type="submit" value="Create Account" />
            </fieldset>
        </form>
        
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/login" class="button secondary">Sign In Instead</a>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
