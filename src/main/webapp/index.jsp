<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
            String username = user.getFullName();
        %>
        
        <%
            String imgmsg = (String) request.getAttribute("imgmsg");
            if (imgmsg!=null){
        %>
            <div class="message success"><%=imgmsg%></div>
        <%
            }
        %>
        
        <h1>Image Gallery Dashboard</h1>
        <h2>Welcome back, <%=username%>!</h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/addimage" class="button">Upload New Image</a>
            <a href="${pageContext.request.contextPath}/viewgallery" class="button">View Gallery</a>
            <a href="${pageContext.request.contextPath}/logout" class="button secondary">Sign Out</a>
        </div>
        
        <div class="dashboard-section mt-lg">
            <h3>Quick Access</h3>
            <p>From here you can manage your image collection. Upload new images, view your gallery, or sign out of your account.</p>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>