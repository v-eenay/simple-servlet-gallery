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
    <!-- Primary CSS path -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <!-- Fallback CSS paths -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Inline critical CSS as a fallback -->
    <style>
        body { font-family: 'Work Sans', sans-serif; background-color: #f5f5f5; color: #232323; line-height: 1.6; }
        .container { width: 90%; max-width: 500px; margin: 0 auto; padding: 2rem 1rem; }
        h1 { font-size: 2.5rem; margin-bottom: 0.5rem; color: #1d1d1d; text-align: center; }
        h2 { font-size: 1.75rem; color: #3498db; margin-bottom: 2rem; font-weight: 400; text-align: center; }
        fieldset { border: none; padding: 2rem; margin-bottom: 2rem; border-radius: 2px; background-color: #fefefe; box-shadow: 0 1px 3px rgba(0,0,0,0.08); }
        legend { font-weight: 700; padding: 0 0.5rem; color: #2c3e50; }
        label { display: block; margin-bottom: 1rem; font-weight: 600; color: #1d1d1d; }
        input[type="email"], input[type="password"] { width: 100%; padding: 0.5rem; margin-top: 0.25rem; border: none; border-bottom: 1px solid rgba(0,0,0,0.1); font-size: 1rem; transition: border 0.3s ease; background-color: transparent; }
        input[type="email"]:focus, input[type="password"]:focus { outline: none; border-bottom: 1px solid #2c3e50; }
        input[type="submit"] { display: block; width: 100%; padding: 1rem; background-color: transparent; color: #2c3e50; border: 1px solid #2c3e50; border-radius: 2px; font-size: 1rem; text-transform: uppercase; letter-spacing: 0.05em; cursor: pointer; transition: all 0.3s ease; margin-top: 2rem; }
        input[type="submit"]:hover { background-color: #2c3e50; color: #fefefe; }
        .message { padding: 1rem; margin: 1rem 0; border-radius: 2px; background-color: #fefefe; border-left: 2px solid #2c3e50; box-shadow: 0 1px 3px rgba(0,0,0,0.08); }
        .message.error { border-left-color: #e74c3c; background-color: rgba(231, 76, 60, 0.05); }
        .message.success { border-left-color: #27ae60; background-color: rgba(39, 174, 96, 0.05); }
        .nav-links { display: flex; justify-content: center; margin: 2rem 0; }
        .button { display: inline-block; padding: 0.5rem 1rem; background-color: transparent; color: #34495e; border: 1px solid #34495e; border-radius: 2px; font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em; cursor: pointer; transition: all 0.3s ease; text-decoration: none; }
        .button:hover { background-color: #34495e; color: #fefefe; }
    </style>
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
