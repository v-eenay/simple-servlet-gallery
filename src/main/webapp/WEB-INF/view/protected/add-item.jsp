<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="com.example.verysimpleimagegallery.model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        .tag-container {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            margin-top: 0.5rem;
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            min-height: 40px;
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
    </style>
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

                <label>
                    Tags
                    <div class="tag-container" id="tagContainer">
                        <!-- Tags will be added here dynamically -->
                        <input type="text" id="tagInput" class="tag-input" placeholder="Add tags (comma separated)..." />
                    </div>
                    <input type="hidden" id="tags" name="tags" value="" />
                    <div class="field-help">Add tags to help categorize your image</div>
                </label>

                <div class="tag-suggestions">
                    <%
                        List<Tag> allTags = (List<Tag>) request.getAttribute("allTags");
                        if (allTags != null && !allTags.isEmpty()) {
                            for (Tag tag : allTags) {
                    %>
                        <span class="tag-suggestion" data-tag="<%=tag.getName()%>"><%=tag.getName()%></span>
                    <%
                            }
                        }
                    %>
                </div>
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
    <script>
        // Tag management
        document.addEventListener('DOMContentLoaded', function() {
            const tagInput = document.getElementById('tagInput');
            const tagContainer = document.getElementById('tagContainer');
            const tagsField = document.getElementById('tags');
            const tagSuggestions = document.querySelectorAll('.tag-suggestion');

            // Add tag when Enter is pressed or comma is typed
            tagInput.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' || e.key === ',') {
                    e.preventDefault();
                    const tag = tagInput.value.trim().replace(/,/g, '');
                    if (tag) {
                        addTag(tag);
                        tagInput.value = '';
                        updateTagsField();
                    }
                }
            });

            // Add tag when input loses focus
            tagInput.addEventListener('blur', function() {
                const tag = tagInput.value.trim().replace(/,/g, '');
                if (tag) {
                    addTag(tag);
                    tagInput.value = '';
                    updateTagsField();
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
                removeBtn.addEventListener('click', function(e) {
                    e.stopPropagation();
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
        });
    </script>
</body>
</html>
