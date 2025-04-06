<%--
  Admin Gallery - Allows admins to view and manage all gallery items
--%>
<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="com.example.verysimpleimagegallery.model.GalleryItem" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Gallery - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
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
            
            String error = request.getParameter("error");
            if (error == null) {
                error = (String) request.getAttribute("error");
            }
            if (error != null) {
        %>
            <div class="message error"><%=error%></div>
        <%
            }
            
            User currentUser = (User) session.getAttribute("user");
            List<GalleryItem> galleryItems = (List<GalleryItem>) request.getAttribute("galleryItems");
        %>
        
        <h1>Image Gallery Management</h1>
        <h2>View and manage all user images</h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="button">Back to Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/list-users" class="button">Manage Users</a>
            <a href="${pageContext.request.contextPath}/" class="button">Gallery Home</a>
        </div>
        
        <div class="gallery-filters">
            <div class="search-box">
                <input type="text" id="gallerySearch" placeholder="Search by title or user..." />
            </div>
        </div>

        <% if (galleryItems == null || galleryItems.isEmpty()) { %>
            <p class="empty-gallery">No images have been uploaded yet.</p>
        <% } else { %>
            <div class="gallery-grid admin-gallery">
                <% for (GalleryItem item : galleryItems) { %>
                    <div class="gallery-item" data-title="<%=item.getTitle().toLowerCase()%>" data-user="<%=item.getUserName().toLowerCase()%>">
                        <img src="data:image/jpeg;base64,<%=item.getBase64Image()%>" alt="<%=item.getTitle()%>" class="loaded" />
                        <div class="item-info">
                            <h3 class="item-title"><%=item.getTitle()%></h3>
                            <p class="item-owner">Uploaded by: <%=item.getUserName()%></p>
                        </div>
                        <div class="item-actions">
                            <form action="${pageContext.request.contextPath}/admin/gallery" method="post" style="display: inline; margin: 0">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="itemId" value="<%=item.getId()%>" />
                                <button type="submit" class="button small" onclick="return confirm('Are you sure you want to delete this image?')">Delete</button>
                            </form>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } %>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
    <script>
        // Gallery search functionality
        document.getElementById('gallerySearch').addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const items = document.querySelectorAll('.gallery-item');
            
            items.forEach(item => {
                const title = item.getAttribute('data-title');
                const user = item.getAttribute('data-user');
                
                if (title.includes(searchTerm) || user.includes(searchTerm)) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    </script>
</body>
</html> 