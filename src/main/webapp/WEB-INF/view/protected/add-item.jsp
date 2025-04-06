<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
            String username = user.getFullName();
        %>
        <h1>Upload Image</h1>
        <h2>Add to your collection</h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/viewgallery" class="button">View Gallery</a>
            <a href="${pageContext.request.contextPath}/" class="button">Dashboard</a>
        </div>

        <form action="${pageContext.request.contextPath}/addimage" method="post" enctype="multipart/form-data">
            <fieldset>
                <legend>Image Details</legend>
                <label>
                    Image Title
                    <input type="text" name="title" required placeholder="Enter title" />
                </label>
                <div class="upload-container">
                    <div class="upload-zone">
                        <div class="upload-prompt">
                            <svg class="upload-icon" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
                                <polyline points="17 8 12 3 7 8"/>
                                <line x1="12" y1="3" x2="12" y2="15"/>
                            </svg>
                            <span>Drag & drop your image here<br>or click to browse</span>
                        </div>
                        <div class="preview-container" style="display: none;">
                            <img id="imagePreview" src="#" alt="Preview" />
                            <button type="button" class="remove-preview" aria-label="Remove image">&times;</button>
                        </div>
                    </div>
                    <input type="file" id="imageInput" name="image" required accept="image/*" />
                    <div class="field-help">Supported: JPEG, PNG, GIF (Max size: 5MB)</div>
                </div>
                <input type="submit" value="Upload" />
            </fieldset>
        </form>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>
