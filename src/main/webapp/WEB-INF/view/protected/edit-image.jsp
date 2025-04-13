<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="com.example.verysimpleimagegallery.model.GalleryItem" %>
<%@ page import="com.example.verysimpleimagegallery.model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Image - Image Gallery</title>
    <!-- Primary CSS path -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <!-- Fallback CSS paths -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <style>
        .edit-image-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 2rem;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        
        .image-preview {
            width: 100%;
            max-height: 400px;
            overflow: hidden;
            margin-bottom: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        
        .image-preview img {
            width: 100%;
            height: auto;
            display: block;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #2c3e50;
        }
        
        .form-control {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-family: inherit;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
        }
        
        .tag-container {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            margin-top: 0.5rem;
        }
        
        .tag {
            display: inline-flex;
            align-items: center;
            background-color: #f1f1f1;
            padding: 0.25rem 0.75rem;
            border-radius: 20px;
            font-size: 0.9rem;
            transition: all 0.3s ease;
        }
        
        .tag:hover {
            background-color: #e0e0e0;
        }
        
        .tag .remove-tag {
            margin-left: 0.5rem;
            cursor: pointer;
            color: #7f8c8d;
        }
        
        .tag .remove-tag:hover {
            color: #e74c3c;
        }
        
        .tag-input {
            flex: 1;
            min-width: 150px;
            border: none;
            padding: 0.5rem;
            font-family: inherit;
            font-size: 0.9rem;
            background-color: transparent;
        }
        
        .tag-input:focus {
            outline: none;
        }
        
        .tag-suggestions {
            margin-top: 0.5rem;
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
        }
        
        .tag-suggestion {
            background-color: #f8f9fa;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.8rem;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .tag-suggestion:hover {
            background-color: #3498db;
            color: white;
        }
        
        .file-upload {
            position: relative;
            display: inline-block;
            width: 100%;
        }
        
        .file-upload-label {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            padding: 1rem;
            background-color: #f8f9fa;
            border: 2px dashed #ddd;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .file-upload-label:hover {
            background-color: #f1f1f1;
            border-color: #3498db;
        }
        
        .file-upload-label i {
            margin-right: 0.5rem;
            font-size: 1.2rem;
            color: #3498db;
        }
        
        .file-upload input[type="file"] {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            opacity: 0;
            cursor: pointer;
        }
        
        .button-group {
            display: flex;
            gap: 1rem;
            margin-top: 2rem;
        }
        
        .button-group .button {
            flex: 1;
        }
    </style>
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
            GalleryItem galleryItem = (GalleryItem) request.getAttribute("galleryItem");
            List<Tag> allTags = (List<Tag>) request.getAttribute("allTags");
            
            if (galleryItem == null) {
                response.sendRedirect(request.getContextPath() + "/viewgallery?error=Image not found");
                return;
            }
            
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="message error"><%=error%></div>
        <%
            }
        %>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/viewgallery" class="button"><i class="fas fa-arrow-left"></i> Back to Gallery</a>
            <a href="${pageContext.request.contextPath}/home" class="button"><i class="fas fa-home"></i> Dashboard</a>
            <% if (user.getRole() == 0 || user.getRole() == 2) { %>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="button"><i class="fas fa-user-shield"></i> Admin Dashboard</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/logout" class="button secondary logout-btn"><i class="fas fa-sign-out-alt"></i> Sign Out</a>
        </div>

        <h1 class="page-title">Edit Image</h1>
        
        <div class="edit-image-container">
            <div class="image-preview">
                <img src="${pageContext.request.contextPath}/imagedisplay?id=<%=galleryItem.getId()%>" alt="<%=galleryItem.getTitle()%>">
            </div>
            
            <form action="${pageContext.request.contextPath}/editimage" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="<%=galleryItem.getId()%>">
                
                <div class="form-group">
                    <label for="title">Image Title</label>
                    <input type="text" id="title" name="title" class="form-control" value="<%=galleryItem.getTitle()%>" required>
                </div>
                
                <div class="form-group">
                    <label for="tags">Tags</label>
                    <div class="tag-input-container form-control">
                        <div class="tag-container" id="tagContainer">
                            <!-- Tags will be added here dynamically -->
                        </div>
                        <input type="text" id="tagInput" class="tag-input" placeholder="Add tags...">
                        <input type="hidden" id="tags" name="tags" value="<%=galleryItem.getTagsAsString()%>">
                    </div>
                    
                    <div class="tag-suggestions">
                        <% if (allTags != null && !allTags.isEmpty()) { 
                            for (Tag tag : allTags) { %>
                                <span class="tag-suggestion" data-tag="<%=tag.getName()%>"><%=tag.getName()%></span>
                            <% } 
                        } %>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="image">Replace Image (Optional)</label>
                    <div class="file-upload">
                        <label for="image" class="file-upload-label">
                            <i class="fas fa-upload"></i> Choose a new image file
                        </label>
                        <input type="file" id="image" name="image" accept="image/*">
                    </div>
                    <div id="fileNameDisplay" class="file-name"></div>
                </div>
                
                <div class="button-group">
                    <a href="${pageContext.request.contextPath}/viewgallery" class="button secondary">Cancel</a>
                    <button type="submit" class="button">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        // Tag management
        document.addEventListener('DOMContentLoaded', function() {
            const tagInput = document.getElementById('tagInput');
            const tagContainer = document.getElementById('tagContainer');
            const tagsField = document.getElementById('tags');
            const tagSuggestions = document.querySelectorAll('.tag-suggestion');
            
            // Initialize tags from the hidden field
            const initialTags = tagsField.value.split(',').filter(tag => tag.trim() !== '');
            initialTags.forEach(tag => addTag(tag.trim()));
            
            // Add tag when Enter is pressed
            tagInput.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' || e.key === ',') {
                    e.preventDefault();
                    const tag = tagInput.value.trim();
                    if (tag) {
                        addTag(tag);
                        tagInput.value = '';
                        updateTagsField();
                    }
                }
            });
            
            // Add tag when clicking on a suggestion
            tagSuggestions.forEach(suggestion => {
                suggestion.addEventListener('click', function() {
                    const tag = this.dataset.tag;
                    if (!tagExists(tag)) {
                        addTag(tag);
                        updateTagsField();
                    }
                });
            });
            
            // Function to add a tag
            function addTag(text) {
                if (tagExists(text)) return;
                
                const tag = document.createElement('span');
                tag.className = 'tag';
                tag.textContent = text;
                
                const removeBtn = document.createElement('span');
                removeBtn.className = 'remove-tag';
                removeBtn.innerHTML = '&times;';
                removeBtn.addEventListener('click', function() {
                    tag.remove();
                    updateTagsField();
                });
                
                tag.appendChild(removeBtn);
                tagContainer.insertBefore(tag, tagInput);
            }
            
            // Function to check if a tag already exists
            function tagExists(text) {
                const tags = tagContainer.querySelectorAll('.tag');
                for (let i = 0; i < tags.length; i++) {
                    if (tags[i].textContent.slice(0, -1).toLowerCase() === text.toLowerCase()) {
                        return true;
                    }
                }
                return false;
            }
            
            // Function to update the hidden tags field
            function updateTagsField() {
                const tags = tagContainer.querySelectorAll('.tag');
                const tagValues = Array.from(tags).map(tag => tag.textContent.slice(0, -1).trim());
                tagsField.value = tagValues.join(',');
            }
            
            // Display selected file name
            const fileInput = document.getElementById('image');
            const fileNameDisplay = document.getElementById('fileNameDisplay');
            
            fileInput.addEventListener('change', function() {
                if (this.files && this.files[0]) {
                    fileNameDisplay.textContent = this.files[0].name;
                } else {
                    fileNameDisplay.textContent = '';
                }
            });
        });
    </script>
</body>
</html>
