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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* Additional styles for admin gallery */
        .admin-gallery .gallery-item {
            height: auto;
            display: flex;
            flex-direction: column;
        }

        .admin-gallery .gallery-item img {
            height: 200px;
            object-fit: cover;
        }

        .item-actions {
            display: flex;
            gap: 8px;
            padding: 8px 16px 16px;
            justify-content: center;
        }

        .button.small {
            font-size: 0.8rem;
            padding: 4px 8px;
        }

        .button.view {
            background-color: var(--color-accent);
            color: white;
            border-color: var(--color-accent);
        }

        .button.delete {
            background-color: var(--color-error);
            color: white;
            border-color: var(--color-error);
        }

        /* Modal styles */
        .image-modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.8);
            z-index: 1000;
            overflow: auto;
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background-color: white;
            margin: auto;
            padding: 20px;
            border-radius: 8px;
            max-width: 800px;
            width: 90%;
            position: relative;
            animation: modalFadeIn 0.3s;
        }

        @keyframes modalFadeIn {
            from { opacity: 0; transform: translateY(-20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .modal-close {
            position: absolute;
            top: 10px;
            right: 10px;
            font-size: 24px;
            cursor: pointer;
            color: #888;
        }

        .modal-close:hover {
            color: #333;
        }

        .modal-header {
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }

        .modal-body {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        @media (min-width: 768px) {
            .modal-body {
                flex-direction: row;
            }
        }

        .modal-image {
            flex: 1;
            text-align: center;
        }

        .modal-image img {
            max-width: 100%;
            max-height: 400px;
            object-fit: contain;
            border-radius: 4px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .modal-details {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .detail-item {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .detail-label {
            font-weight: bold;
            color: #666;
            font-size: 0.9rem;
        }

        .detail-value {
            font-size: 1rem;
        }

        .modal-actions {
            margin-top: 20px;
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }
    </style>
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
                    <div class="gallery-item" data-title="<%=item.getTitle().toLowerCase()%>" data-user="<%=item.getUserName().toLowerCase()%>" data-id="<%=item.getId()%>">
                        <img src="${pageContext.request.contextPath}/imagedisplay?id=<%=item.getId()%>" alt="<%=item.getTitle()%>" class="loaded" />
                        <div class="item-info">
                            <h3 class="item-title"><%=item.getTitle()%></h3>
                            <p class="item-owner">Uploaded by: <%=item.getUserName()%></p>
                        </div>
                        <div class="item-actions">
                            <button type="button" class="button small view" onclick="openImageDetails(<%=item.getId()%>, '<%=item.getTitle()%>', '<%=item.getUserName()%>')"><i class="fas fa-eye"></i> View Details</button>
                            <form action="${pageContext.request.contextPath}/admin/gallery" method="post" style="display: inline; margin: 0">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="itemId" value="<%=item.getId()%>" />
                                <button type="submit" class="button small delete" onclick="return confirmDelete(event, '<%=item.getTitle()%>')"><i class="fas fa-trash"></i> Delete</button>
                            </form>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } %>

        <!-- Image Details Modal -->
        <div id="imageDetailsModal" class="image-modal">
            <div class="modal-content">
                <span class="modal-close" onclick="closeImageDetails()">&times;</span>
                <div class="modal-header">
                    <h2 id="modalTitle">Image Details</h2>
                </div>
                <div class="modal-body">
                    <div class="modal-image">
                        <img id="modalImage" src="" alt="Image Preview">
                    </div>
                    <div class="modal-details">
                        <div class="detail-item">
                            <div class="detail-label">Title</div>
                            <div id="imageTitle" class="detail-value"></div>
                        </div>
                        <div class="detail-item">
                            <div class="detail-label">Uploaded By</div>
                            <div id="imageUploader" class="detail-value"></div>
                        </div>
                        <div class="detail-item">
                            <div class="detail-label">Image ID</div>
                            <div id="imageId" class="detail-value"></div>
                        </div>
                        <div class="detail-item">
                            <div class="detail-label">Image Size</div>
                            <div id="imageSize" class="detail-value">Calculating...</div>
                        </div>
                        <div class="detail-item">
                            <div class="detail-label">Image Dimensions</div>
                            <div id="imageDimensions" class="detail-value">Loading...</div>
                        </div>
                    </div>
                </div>
                <div class="modal-actions">
                    <form id="deleteForm" action="${pageContext.request.contextPath}/admin/gallery" method="post" style="display: inline; margin: 0">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" id="deleteItemId" name="itemId" value="" />
                        <button type="submit" class="button delete" onclick="return confirmDelete(event, document.getElementById('imageTitle').textContent)"><i class="fas fa-trash"></i> Delete Image</button>
                    </form>
                    <button type="button" class="button" onclick="closeImageDetails()">Close</button>
                </div>
            </div>
        </div>
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

        // Image details modal functionality
        function openImageDetails(id, title, uploader) {
            // Set modal content
            document.getElementById('modalTitle').textContent = 'Image Details: ' + title;
            document.getElementById('imageTitle').textContent = title;
            document.getElementById('imageUploader').textContent = uploader;
            document.getElementById('imageId').textContent = id;
            document.getElementById('deleteItemId').value = id;

            // Set image source
            const imageSrc = '${pageContext.request.contextPath}/imagedisplay?id=' + id;
            const modalImage = document.getElementById('modalImage');
            modalImage.src = imageSrc;

            // Calculate image dimensions and size when loaded
            modalImage.onload = function() {
                document.getElementById('imageDimensions').textContent = this.naturalWidth + ' × ' + this.naturalHeight + ' px';

                // Fetch image size
                fetch(imageSrc)
                    .then(response => {
                        const size = response.headers.get('content-length');
                        if (size) {
                            const sizeInKB = Math.round(size / 1024);
                            document.getElementById('imageSize').textContent = sizeInKB + ' KB';
                        } else {
                            document.getElementById('imageSize').textContent = 'Unknown';
                        }
                    })
                    .catch(() => {
                        document.getElementById('imageSize').textContent = 'Unknown';
                    });
            };

            // Show modal
            const modal = document.getElementById('imageDetailsModal');
            modal.style.display = 'flex';

            // Add event listener to close modal when clicking outside
            modal.onclick = function(event) {
                if (event.target === modal) {
                    closeImageDetails();
                }
            };
        }

        function closeImageDetails() {
            document.getElementById('imageDetailsModal').style.display = 'none';
        }

        function confirmDelete(event, title) {
            return confirm('Are you sure you want to delete the image "' + title + '"? This action cannot be undone.');
        }

        // Close modal with Escape key
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                closeImageDetails();
            }
        });
    </script>
</body>
</html>