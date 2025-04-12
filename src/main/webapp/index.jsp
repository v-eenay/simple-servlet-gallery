<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.verysimpleimagegallery.model.GalleryItem" %>
<%@ page import="com.example.verysimpleimagegallery.model.ActivityLog" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Image Gallery</title>
    <!-- Primary CSS path -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <!-- Fallback CSS paths -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <!-- Inline critical CSS as a fallback -->
    <style>
        body { font-family: 'Work Sans', sans-serif; background-color: #f5f5f5; color: #232323; line-height: 1.6; }
        .container { width: 90%; max-width: 1200px; margin: 0 auto; padding: 2rem 1rem; }
        .nav-links { display: flex; flex-wrap: wrap; gap: 1rem; margin: 2rem 0; padding: 1rem 0; border-top: 1px solid rgba(0,0,0,0.05); border-bottom: 1px solid rgba(0,0,0,0.05); }
        .button { display: inline-block; padding: 0.5rem 1rem; background-color: transparent; color: #2c3e50; border: 1px solid #2c3e50; border-radius: 2px; font-family: 'Space Mono', monospace; font-size: 0.9rem; text-transform: uppercase; letter-spacing: 0.05em; cursor: pointer; transition: all 0.3s ease; text-decoration: none; }
        .button:hover { background-color: #2c3e50; color: #fefefe; transform: translateY(-1px); box-shadow: 0 2px 6px rgba(0,0,0,0.12); text-decoration: none; }
        .button.secondary { background-color: transparent; color: #34495e; border: 1px solid #34495e; }
        .button.secondary:hover { background-color: #34495e; color: #fefefe; }
        .logout-btn { margin-left: auto; }
        h1, h2, h3, h4, h5, h6 { font-family: 'Space Mono', monospace; font-weight: 700; line-height: 1.2; margin-bottom: 1rem; letter-spacing: -0.02em; }
        h1 { font-size: 2.5rem; margin-bottom: 0.5rem; color: #1d1d1d; }
        h2 { font-size: 1.75rem; color: #3498db; margin-bottom: 2rem; font-weight: 400; }
        .gallery-title { margin-top: 4rem; text-align: center; font-size: 2.5rem; color: #1d1d1d; position: relative; }
        .gallery-subtitle { text-align: center; font-weight: 300; margin-bottom: 4rem; color: #3498db; }
        .dashboard-section { display: grid; grid-template-columns: 1fr; gap: 2rem; margin-top: 2rem; }
        @media (min-width: 768px) { .dashboard-section { grid-template-columns: 3.5fr 1fr; } }
        .gallery-content { background-color: #fefefe; border-radius: 2px; padding: 2rem; box-shadow: 0 1px 3px rgba(0,0,0,0.08); }
        .recent-activities { background-color: #fefefe; border-radius: 2px; padding: 1rem; box-shadow: 0 1px 3px rgba(0,0,0,0.08); max-height: 450px; overflow: hidden; display: flex; flex-direction: column; border-top: 3px solid #2c3e50; position: relative; }
    </style>
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
            String username = user.getFullName();
        %>

        <%
            String imgmsg = (String) request.getAttribute("imgmsg");
            if (imgmsg != null) {
        %>
            <div class="message success"><%=imgmsg%></div>
        <%
            }
            String errormsg = (String) request.getAttribute("errormsg");
            if (errormsg != null) {
        %>
            <div class="message error"><%=errormsg%></div>
        <%
            }
        %>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/addimage" class="button"><i class="fas fa-upload"></i> Upload Image</a>
            <a href="${pageContext.request.contextPath}/viewgallery" class="button"><i class="fas fa-images"></i> View Gallery</a>
            <% if (user.getRole() == 0 || user.getRole() == 2) { %>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="button"><i class="fas fa-user-shield"></i> Admin Dashboard</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/logout" class="button secondary logout-btn"><i class="fas fa-sign-out-alt"></i> Sign Out</a>
        </div>

        <h1 class="gallery-title">Image Gallery</h1>
        <h2 class="gallery-subtitle">Welcome, <%=username%></h2>

        <div class="dashboard-section">
            <div class="gallery-content">
                <h3><i class="fas fa-photo-film"></i> Your Photo Gallery</h3>
                <p>Upload new images or browse your gallery to manage your collection.</p>
                <p>Our gallery provides a sophisticated space for your cherished images.</p>

                <div class="quick-actions">
                    <h4>Quick Actions</h4>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/addimage" class="button"><i class="fas fa-upload"></i> Upload New Image</a>
                        <a href="${pageContext.request.contextPath}/viewgallery" class="button"><i class="fas fa-images"></i> View My Gallery</a>
                    </div>
                </div>

                <div class="featured-gallery">
                    <h4><i class="fas fa-star"></i> Featured Gallery</h4>
                    <p>Explore our curated collection of featured images from our community.</p>

                    <%
                        ArrayList<GalleryItem> recentActivities = (ArrayList<GalleryItem>) request.getAttribute("recentActivities");
                        if (recentActivities != null && !recentActivities.isEmpty() && recentActivities.size() >= 4) {
                    %>
                    <div class="featured-grid">
                        <% for(int i = 0; i < Math.min(4, recentActivities.size()); i++) {
                            GalleryItem item = recentActivities.get(i);
                        %>
                            <div class="featured-item">
                                <img src="${pageContext.request.contextPath}/imagedisplay?id=<%=item.getId()%>" alt="<%=item.getTitle()%>">
                                <div class="featured-overlay">
                                    <h5><%=item.getTitle()%></h5>
                                    <p>By <%=item.getUserName()%></p>
                                </div>
                            </div>
                        <% } %>
                    </div>
                    <% } else { %>
                        <p class="empty-message">No featured images available yet. Be the first to upload!</p>
                    <% } %>
                </div>
            </div>

            <div class="recent-activities">
                <h3><i class="fas fa-clock-rotate-left"></i> Recent Activity</h3>
                <%
                    List<ActivityLog> activityLogs = (List<ActivityLog>) request.getAttribute("activityLogs");
                    if (activityLogs != null && !activityLogs.isEmpty()) {
                %>
                <div class="activity-list">
                    <% for(ActivityLog log : activityLogs) {
                        String activityTypeClass = log.getActivityType() != null ? "activity-" + log.getActivityType() : "";
                    %>
                        <div class="activity-item <%= activityTypeClass %>">
                            <% if (log.getActivityType() != null && log.getActivityType().equals("upload")) { %>
                                <div class="activity-icon">📷</div>
                            <% } else if (log.getActivityType() != null && log.getActivityType().equals("login")) { %>
                                <div class="activity-icon">🔑</div>
                            <% } else if (log.getActivityType() != null && log.getActivityType().equals("admin")) { %>
                                <div class="activity-icon">👑</div>
                            <% } else if (log.getActivityType() != null && log.getActivityType().equals("system")) { %>
                                <div class="activity-icon">⚙️</div>
                            <% } else { %>
                                <div class="activity-icon">📝</div>
                            <% } %>
                            <div class="activity-details">
                                <div class="activity-time"><%= log.getFormattedTime() %></div>
                                <div class="activity-content"><%= log.getActivity() %></div>
                            </div>
                        </div>
                    <% } %>
                </div>
                <% } else { %>
                    <div class="activity-item">
                        <div class="activity-icon">❓</div>
                        <div class="activity-details">
                            <div class="activity-content">No recent activities found</div>
                        </div>
                    </div>
                <% } %>

                <h3 class="mt-lg"><i class="fas fa-images"></i> Recent Uploads</h3>
                <%
                    if (recentActivities != null && !recentActivities.isEmpty()) {
                %>
                <div class="activity-list">
                    <% for(GalleryItem activity: recentActivities) { %>
                        <div class="activity-item activity-upload">
                            <div class="activity-thumbnail">
                                <img src="${pageContext.request.contextPath}/imagedisplay?id=<%=activity.getId()%>" alt="<%=activity.getTitle()%>">
                            </div>
                            <div class="activity-details">
                                <div class="activity-time">Recent</div>
                                <div class="activity-content">
                                    <strong><%=activity.getTitle()%></strong> uploaded by <span class="activity-user"><%=activity.getUserName()%></span>
                                </div>
                            </div>
                        </div>
                    <% } %>
                </div>
                <% } else { %>
                    <p>No recent uploads</p>
                <% } %>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>