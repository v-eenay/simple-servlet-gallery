<%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/6/2025
  Time: 11:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="com.example.verysimpleimagegallery.model.ActivityLog" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* Enhanced styles for stat cards */
        .admin-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin: 20px 0 30px;
        }

        .stat-card {
            background-color: #fff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            text-align: center;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .stat-card:nth-child(1) {
            border-top: 3px solid #3498db;
        }

        .stat-card:nth-child(2) {
            border-top: 3px solid #9b59b6;
        }

        .stat-card:nth-child(3) {
            border-top: 3px solid #2ecc71;
        }

        .stat-card:nth-child(4) {
            border-top: 3px solid #f1c40f;
        }

        .stat-icon {
            font-size: 2rem;
            margin-bottom: 10px;
            color: #7f8c8d;
        }

        .stat-card:nth-child(1) .stat-icon { color: #3498db; }
        .stat-card:nth-child(2) .stat-icon { color: #9b59b6; }
        .stat-card:nth-child(3) .stat-icon { color: #2ecc71; }
        .stat-card:nth-child(4) .stat-icon { color: #f1c40f; }

        .stat-number {
            font-size: 2.5rem;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 5px;
        }

        .stat-title {
            font-size: 0.9rem;
            color: #7f8c8d;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        /* Enhanced styles for activity log */
        .recent-activities {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            padding: 20px;
            border-top: 3px solid #3498db;
        }

        .recent-activities h3 {
            margin-top: 0;
            color: #2c3e50;
            font-size: 1.2rem;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .activity-list {
            margin-top: 15px;
            max-height: 400px;
            overflow-y: auto;
        }

        .activity-item {
            display: flex;
            padding: 12px;
            margin-bottom: 10px;
            background-color: #f8f9fa;
            border-radius: 6px;
            transition: all 0.3s ease;
            border-left: 3px solid #ddd;
        }

        .activity-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.05);
        }

        .activity-upload { border-left-color: #27ae60; }
        .activity-login { border-left-color: #f39c12; }
        .activity-admin { border-left-color: #8e44ad; }
        .activity-delete { border-left-color: #e74c3c; }
        .activity-system { border-left-color: #3498db; }

        .activity-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background-color: #eee;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 12px;
            flex-shrink: 0;
        }

        .activity-upload .activity-icon { background-color: rgba(39, 174, 96, 0.1); color: #27ae60; }
        .activity-login .activity-icon { background-color: rgba(243, 156, 18, 0.1); color: #f39c12; }
        .activity-admin .activity-icon { background-color: rgba(142, 68, 173, 0.1); color: #8e44ad; }
        .activity-delete .activity-icon { background-color: rgba(231, 76, 60, 0.1); color: #e74c3c; }
        .activity-system .activity-icon { background-color: rgba(52, 152, 219, 0.1); color: #3498db; }

        .activity-icon i {
            font-size: 1.2rem;
        }

        .activity-details {
            flex: 1;
        }

        .activity-time {
            font-size: 0.75rem;
            color: #7f8c8d;
            margin-bottom: 4px;
        }

        .activity-content {
            font-size: 0.9rem;
            color: #2c3e50;
            margin-bottom: 4px;
        }

        .activity-user {
            font-size: 0.8rem;
            color: #7f8c8d;
        }

        .activity-user strong {
            color: #3498db;
        }

        .empty-activity {
            border-left-color: #e74c3c;
            background-color: rgba(231, 76, 60, 0.05);
        }

        .empty-activity .activity-icon {
            background-color: rgba(231, 76, 60, 0.1);
            color: #e74c3c;
        }

        .activity-help {
            font-size: 0.8rem;
            color: #95a5a6;
            margin-top: 4px;
        }

        .view-all-link {
            text-align: center;
            margin-top: 15px;
            padding-top: 10px;
            border-top: 1px solid #eee;
        }

        .button.small {
            font-size: 0.8rem;
            padding: 5px 10px;
        }

        /* Dashboard section styling */
        .dashboard-section {
            display: grid;
            grid-template-columns: 1fr;
            gap: 20px;
            margin-top: 30px;
        }

        @media (min-width: 768px) {
            .dashboard-section {
                grid-template-columns: 1.5fr 1fr;
            }
        }

        .admin-content {
            background-color: #fff;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .admin-content h3 {
            margin-top: 0;
            color: #2c3e50;
            font-size: 1.2rem;
            margin-bottom: 15px;
        }

        .admin-content p {
            color: #7f8c8d;
            line-height: 1.6;
            margin-bottom: 20px;
        }

        .quick-actions {
            background-color: #f8f9fa;
            border-radius: 6px;
            padding: 15px;
            margin-top: 20px;
        }

        .quick-actions h4 {
            margin-top: 0;
            color: #2c3e50;
            font-size: 1rem;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .action-buttons {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .user-name {
            color: #3498db;
            font-weight: 500;
        }

        .mt-lg {
            margin-top: 30px;
        }
    </style>
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
        <h2>Welcome, <span class="user-name"><%=username%></span></h2>

        <div class="admin-stats">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-users"></i></div>
                <div class="stat-number"><%=totalUsers != null ? totalUsers : 0%></div>
                <div class="stat-title">Total Users</div>
            </div>

            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-user-shield"></i></div>
                <div class="stat-number"><%=adminCount != null ? adminCount : 0%></div>
                <div class="stat-title">Admins</div>
            </div>

            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-user"></i></div>
                <div class="stat-number"><%=totalUsers != null && adminCount != null ? (totalUsers - adminCount) : 0%></div>
                <div class="stat-title">Regular Users</div>
            </div>

            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-images"></i></div>
                <div class="stat-number"><%=imageCount != null ? imageCount : 0%></div>
                <div class="stat-title">Total Images</div>
            </div>
        </div>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/list-users" class="button"><i class="fas fa-user-cog"></i> Manage Users</a>
            <a href="${pageContext.request.contextPath}/admin/gallery" class="button"><i class="fas fa-photo-video"></i> Manage Gallery</a>
            <% if (user.isSuperAdmin()) { %>
                <a href="${pageContext.request.contextPath}/admin/add-admin" class="button"><i class="fas fa-user-plus"></i> Add Admin</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/" class="button"><i class="fas fa-home"></i> Gallery Home</a>
            <% if (!user.isSuperAdmin() && user.canUploadImages()) { %>
                <a href="${pageContext.request.contextPath}/upload" class="button"><i class="fas fa-upload"></i> Upload Image</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/logout" class="button secondary"><i class="fas fa-sign-out-alt"></i> Sign Out</a>
        </div>

        <div class="dashboard-section mt-lg">
            <div class="admin-content">
                <h3><i class="fas fa-tachometer-alt"></i> Dashboard Overview</h3>
                <p>Welcome to your admin dashboard. From here you can manage users, gallery items, and monitor system activity.</p>

                <div class="quick-actions">
                    <h4><i class="fas fa-bolt"></i> Quick Actions</h4>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/list-users" class="button"><i class="fas fa-user-cog"></i> Manage Users</a>
                        <a href="${pageContext.request.contextPath}/admin/gallery" class="button"><i class="fas fa-photo-video"></i> Manage Gallery</a>
                    </div>
                </div>
            </div>

            <div class="recent-activities">
                <h3><i class="fas fa-history"></i> Recent Activity Log</h3>
                <div class="activity-list">
                    <%
                        List<ActivityLog> activityLogs = (List<ActivityLog>) request.getAttribute("activityLogs");
                        if (activityLogs != null && !activityLogs.isEmpty()) {
                            for (ActivityLog log : activityLogs) {
                                String activityTypeClass = log.getActivityType() != null ? "activity-" + log.getActivityType() : "";
                    %>
                    <div class="activity-item <%= activityTypeClass %>">
                        <% if (log.getActivityType() != null && log.getActivityType().equals("upload")) { %>
                            <div class="activity-icon"><i class="fas fa-image"></i></div>
                        <% } else if (log.getActivityType() != null && log.getActivityType().equals("login")) { %>
                            <div class="activity-icon"><i class="fas fa-sign-in-alt"></i></div>
                        <% } else if (log.getActivityType() != null && log.getActivityType().equals("admin")) { %>
                            <div class="activity-icon"><i class="fas fa-user-shield"></i></div>
                        <% } else if (log.getActivityType() != null && log.getActivityType().equals("delete")) { %>
                            <div class="activity-icon"><i class="fas fa-trash-alt"></i></div>
                        <% } else if (log.getActivityType() != null && log.getActivityType().equals("system")) { %>
                            <div class="activity-icon"><i class="fas fa-cogs"></i></div>
                        <% } else { %>
                            <div class="activity-icon"><i class="fas fa-clipboard-list"></i></div>
                        <% } %>
                        <div class="activity-details">
                            <div class="activity-time"><%= log.getFormattedTime() %></div>
                            <div class="activity-content"><%= log.getActivity() %></div>
                            <div class="activity-user">by <strong><%= log.getUserName() %></strong></div>
                        </div>
                    </div>
                    <%
                            }
                    %>
                    <div class="view-all-link">
                        <a href="#" class="button small"><i class="fas fa-list"></i> View All Activities</a>
                    </div>
                    <%
                        } else {
                    %>
                    <div class="activity-item empty-activity">
                        <div class="activity-icon"><i class="fas fa-exclamation-circle"></i></div>
                        <div class="activity-details">
                            <div class="activity-content">No recent activities found</div>
                            <div class="activity-help">Activities will appear here as users interact with the system</div>
                        </div>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
