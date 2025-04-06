<%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/6/2025
  Time: 11:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Admin - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <%
            String error = request.getParameter("error");
            if (error != null && error.equals("true")) {
        %>
            <div class="message error">Failed to add admin. Email may already be in use.</div>
        <%
            }
        %>
        
        <h1>Add New Admin</h1>
        <h2>Create admin account</h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="button">Back to Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/list-users" class="button">Manage Users</a>
        </div>

        <form action="${pageContext.request.contextPath}/admin/add-admin" method="post">
            <fieldset>
                <legend>Admin Details</legend>
                <label>
                    Full Name
                    <input type="text" name="fullname" required placeholder="Enter admin name" />
                </label>
                <label>
                    Email Address
                    <input type="email" name="email" required placeholder="Enter email" />
                </label>
                <label>
                    Password
                    <input type="password" name="password" required placeholder="Create password" />
                </label>
                <label>
                    Confirm Password
                    <input type="password" name="confirmPassword" required placeholder="Confirm password" />
                </label>
                <label class="checkbox-label">
                    <input type="checkbox" name="superAdmin" />
                    <span>Grant Super Admin Privileges</span>
                </label>
                <input type="submit" value="Create Admin" />
            </fieldset>
        </form>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
