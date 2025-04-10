<%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/6/2025
  Time: 11:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
            String username = user.getFullName();
            
            Integer totalUsers = (Integer) request.getAttribute("totalUsers");
            Integer adminCount = (Integer) request.getAttribute("adminCount");
            Integer imageCount = (Integer) request.getAttribute("imageCount");
            
            String error = request.getParameter("error");
            if (error == null) {
                error = (String) request.getAttribute("error");
            }
            if (error != null) {
        %>
            <div class="message error"><%=error%></div>
        <%
            }
        %>
        
        <%
            String message = request.getParameter("message");
            if (message == null) {
                message = (String) request.getAttribute("message");
            }
            if (message != null) {
        %>
            <div class="message success"><%=message%></div>
        <%
            }
        %>
        
        <h1>Admin Dashboard</h1>
        <h2>Welcome, <%=username%></h2>
        
        <div class="admin-stats">
            <div class="stat-card">
                <div class="stat-number"><%=totalUsers != null ? totalUsers : 0%></div>
                <div class="stat-title">Total Users</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-number"><%=adminCount != null ? adminCount : 0%></div>
                <div class="stat-title">Admins</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-number"><%=totalUsers != null && adminCount != null ? (totalUsers - adminCount) : 0%></div>
                <div class="stat-title">Regular Users</div>
            </div>
            
            <div class="stat-card">
                <div class="stat-number"><%=imageCount != null ? imageCount : 0%></div>
                <div class="stat-title">Total Images</div>
            </div>
        </div>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/list-users" class="button">Manage Users</a>
            <a href="${pageContext.request.contextPath}/admin/gallery" class="button">Manage Gallery</a>
            <% if (user.isSuperAdmin()) { %>
                <a href="${pageContext.request.contextPath}/admin/add-admin" class="button">Add Admin</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/" class="button">Gallery Home</a>
            <% if (!user.isSuperAdmin() && user.canUploadImages()) { %>
                <a href="${pageContext.request.contextPath}/upload" class="button">Upload Image</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/logout" class="button secondary">Sign Out</a>
        </div>
        
        <div class="dashboard-section mt-lg">
            <div class="admin-content">
                <h3>Dashboard Overview</h3>
                <p>Welcome to your admin dashboard. From here you can manage users, gallery items, and monitor system activity.</p>
                
                <div class="quick-actions">
                    <h4>Quick Actions</h4>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/list-users" class="button">Manage Users</a>
                        <a href="${pageContext.request.contextPath}/admin/gallery" class="button">Manage Gallery</a>
                    </div>
                </div>
            </div>
            
            <div class="recent-activities">
                <h3>Recent Activity Log</h3>
                <div class="activity-list">
                    <div class="activity-item">
                        <div class="activity-time">Today, 14:32</div>
                        <div class="activity-content">User <strong>john@example.com</strong> uploaded a new image</div>
                    </div>
                    <div class="activity-item">
                        <div class="activity-time">Today, 12:15</div>
                        <div class="activity-content">User <strong>sarah@example.com</strong> registered</div>
                    </div>
                    <div class="activity-item">
                        <div class="activity-time">Yesterday, 18:42</div>
                        <div class="activity-content">Admin <strong>admin@example.com</strong> deleted an image</div>
                    </div>
                    <div class="activity-item">
                        <div class="activity-time">Yesterday, 15:10</div>
                        <div class="activity-content">System maintenance completed</div>
                    </div>
                    <div class="activity-item">
                        <div class="activity-time">2 days ago</div>
                        <div class="activity-content">User <strong>mike@example.com</strong> updated profile</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
