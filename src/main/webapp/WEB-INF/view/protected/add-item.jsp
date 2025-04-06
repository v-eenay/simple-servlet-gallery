<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Image - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
            String username = user.getFullName();
        %>
        <h1>Upload New Image</h1>
        <h2>Hello, <%=username%></h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/viewgallery" class="button secondary">View Gallery</a>
            <a href="${pageContext.request.contextPath}/" class="button secondary">Back to Dashboard</a>
        </div>

        <form action="${pageContext.request.contextPath}/addimage" method="post" enctype="multipart/form-data">
            <fieldset>
                <legend>Image Details</legend>
                <label>
                    Image Title
                    <input type="text" name="title" required placeholder="Enter a title for your image" />
                </label>
                <label>
                    Image File
                    <input type="file" name="image" required accept="image/*" />
                    <div class="field-help">Supported formats: JPEG, PNG, GIF</div>
                </label>
                <input type="submit" value="Upload Image" class="button" />
            </fieldset>
        </form>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
