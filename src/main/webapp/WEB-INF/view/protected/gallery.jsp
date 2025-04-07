<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.verysimpleimagegallery.model.GalleryItem" %><%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/5/2025
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gallery - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
        %>
        <h1>Image Gallery</h1>
        <h2><%=user.getFullName()%>'s Collection</h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/addimage" class="button">Upload Image</a>
            <a href="${pageContext.request.contextPath}/" class="button">Dashboard</a>
            <% if (user.getRole() == 0 || user.getRole() == 2) { %>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="button">Admin Dashboard</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/logout" class="button secondary">Sign Out</a>
        </div>

        <div class="recent-activities">
            <h3>Recent Activities</h3>
            <%
                ArrayList<GalleryItem> recentActivities = (ArrayList<GalleryItem>) request.getAttribute("recentActivities");
                if (recentActivities != null && !recentActivities.isEmpty()) {
            %>
            <div class="activity-list">
                <% for(GalleryItem activity: recentActivities) { %>
                    <div class="activity-item">
                        <img src="${pageContext.request.contextPath}/imagedisplay?id=<%=activity.getId()%>"
                             alt="<%=activity.getTitle()%>" class="activity-thumbnail">
                        <div class="activity-info">
                            <p class="activity-title"><%=activity.getTitle()%></p>
                            <p class="activity-user">by <%=activity.getUserName()%></p>
                        </div>
                    </div>
                <% } %>
            </div>
            <% } else { %>
                <p>No recent activities</p>
            <% } %>
        </div>

        <%
            ArrayList<GalleryItem> galleryItems = (ArrayList<GalleryItem>) request.getAttribute("galleryItems");
            if (galleryItems != null && !galleryItems.isEmpty()) {
        %>
        <div class="gallery-grid">
            <% for(GalleryItem galleryItem: galleryItems) { %>
                <div class="gallery-item">
                    <img src="${pageContext.request.contextPath}/imagedisplay?id=<%=galleryItem.getId()%>" alt="<%=galleryItem.getTitle()%>">
                    <div class="item-info">
                        <h3 class="item-title"><%=galleryItem.getTitle()%></h3>
                    </div>
                    <div class="item-actions">
                        <a href="${pageContext.request.contextPath}/deleteimage?id=<%=galleryItem.getId()%>" class="button secondary">Delete</a>
                    </div>
                </div>
            <% } %>
        </div>
        <% } else { %>
            <div class="message">
                <p>You haven't uploaded any images yet.</p>
                <p>Get started by clicking "Upload Image" above.</p>
            </div>
        <% } %>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
