<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.verysimpleimagegallery.model.GalleryItem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Gallery - Image Gallery</title>
    <!-- Primary CSS path -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <!-- Fallback CSS paths -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
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
        .gallery-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 2rem; margin-top: 2rem; }
        .gallery-item { background-color: #fefefe; border-radius: 2px; overflow: hidden; box-shadow: 0 1px 3px rgba(0,0,0,0.08); transition: all 0.3s ease; position: relative; cursor: pointer; }
        .gallery-item:hover { transform: scale(1.02); box-shadow: 0 2px 6px rgba(0,0,0,0.12); }
        .gallery-image-container { position: relative; overflow: hidden; height: 220px; }
        .gallery-item img { width: 100%; height: 100%; object-fit: cover; display: block; transition: all 0.3s ease; }
        .item-info { padding: 1rem; }
        .item-title { margin: 0; font-size: 1.1rem; color: #1d1d1d; }
        .recent-activities { background-color: #fefefe; border-radius: 2px; padding: 1rem; box-shadow: 0 1px 3px rgba(0,0,0,0.08); max-height: 450px; overflow: hidden; display: flex; flex-direction: column; border-top: 3px solid #2c3e50; position: relative; }
        .lightbox { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.9); display: flex; justify-content: center; align-items: center; z-index: 1000; opacity: 0; transition: opacity 0.4s ease; }
        .lightbox.active { opacity: 1; }
    </style>
</head>
<body>
    <div class="container">
        <%
            User user = (User) session.getAttribute("user");
        %>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/addimage" class="button"><i class="fas fa-upload"></i> Upload Image</a>
            <a href="${pageContext.request.contextPath}/home" class="button"><i class="fas fa-home"></i> Dashboard</a>
            <% if (user.getRole() == 0 || user.getRole() == 2) { %>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="button"><i class="fas fa-user-shield"></i> Admin Dashboard</a>
            <% } %>
            <a href="${pageContext.request.contextPath}/logout" class="button secondary logout-btn"><i class="fas fa-sign-out-alt"></i> Sign Out</a>
        </div>

        <h1 class="gallery-title">Image Gallery</h1>
        <h2 class="gallery-subtitle"><%=user.getFullName()%>'s Collection</h2>

        <div class="dashboard-section">
            <div class="gallery-content">
                <h3><i class="fas fa-images"></i> Your Images</h3>
                <%
                    ArrayList<GalleryItem> galleryItems = (ArrayList<GalleryItem>) request.getAttribute("galleryItems");
                    if (galleryItems != null && !galleryItems.isEmpty()) {
                %>
                <div class="gallery-grid">
                    <% for(GalleryItem galleryItem: galleryItems) { %>
                        <div class="gallery-item" onclick="openLightbox(<%=galleryItem.getId()%>, '<%=galleryItem.getTitle()%>')">
                            <div class="gallery-image-container">
                                <img src="${pageContext.request.contextPath}/imagedisplay?id=<%=galleryItem.getId()%>" alt="<%=galleryItem.getTitle()%>">
                            </div>
                            <div class="item-info">
                                <h3 class="item-title"><%=galleryItem.getTitle()%></h3>
                            </div>
                            <div class="item-actions">
                                <a href="${pageContext.request.contextPath}/deleteimage?id=<%=galleryItem.getId()%>" class="button secondary" onclick="event.stopPropagation();"><i class="fas fa-trash"></i> Delete</a>
                            </div>
                        </div>
                    <% } %>
                </div>
                <% } else { %>
                    <div class="message">
                        <p><i class="fas fa-info-circle"></i> You haven't uploaded any images yet.</p>
                        <p>Get started by clicking "Upload Image" above.</p>
                    </div>
                <% } %>
            </div>

            <div class="recent-activities">
                <h3><i class="fas fa-clock-rotate-left"></i> Recent Activity</h3>
                <%
                    ArrayList<GalleryItem> recentActivities = (ArrayList<GalleryItem>) request.getAttribute("recentActivities");
                    if (recentActivities != null && !recentActivities.isEmpty()) {
                %>
                <div class="activity-list">
                    <% for(GalleryItem activity: recentActivities) { %>
                        <div class="activity-item">
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
                    <p><i class="fas fa-info-circle"></i> No recent activities</p>
                <% } %>
            </div>
        </div>
    </div>

    <!-- Lightbox for image preview -->
    <div id="imageLightbox" class="lightbox">
        <div class="lightbox-content">
            <button class="lightbox-close" onclick="closeLightbox()">&times;</button>
            <img id="lightboxImage" src="" alt="">
            <h3 id="lightboxTitle" class="lightbox-title"></h3>
        </div>
    </div>

    <!-- Custom script for gallery page - completely bypassing the standard script.js -->
    <script>
    // Immediately remove any existing messages
    (function() {
        // Remove all messages regardless of content
        const messages = document.querySelectorAll('.message');
        messages.forEach(message => {
            if (message.parentNode) {
                message.parentNode.removeChild(message);
            }
        });
    })();

    // Define minimal required functions without activity logging
    document.addEventListener('DOMContentLoaded', function() {
        // Initialize only what we need for this page
        initGalleryItemsForThisPage();
        initMessageDismissal();
        addRetroEffects();

        // Remove any messages that might have appeared
        const messages = document.querySelectorAll('.message');
        messages.forEach(message => {
            if (message.parentNode) {
                message.parentNode.removeChild(message);
            }
        });
    });

    // Simplified gallery initialization just for this page
    function initGalleryItemsForThisPage() {
        const galleryItems = document.querySelectorAll('.gallery-item');

        if (galleryItems.length === 0) return;

        galleryItems.forEach(item => {
            const img = item.querySelector('img');

            // Only add hover effects, don't interfere with the onclick attribute
            item.addEventListener('mouseenter', function() {
                this.style.transform = 'scale(1.02)';
                this.style.transition = 'all 0.5s ease';
                if (img) {
                    img.style.filter = 'brightness(1.05)';
                    img.style.transition = 'all 0.5s ease';
                }
            });

            item.addEventListener('mouseleave', function() {
                this.style.transform = 'scale(1)';
                this.style.transition = 'all 0.5s ease';
                if (img) {
                    img.style.filter = 'brightness(1)';
                    img.style.transition = 'all 0.5s ease';
                }
            });

            // Make sure the delete button works
            const deleteBtn = item.querySelector('.button.secondary');
            if (deleteBtn) {
                deleteBtn.addEventListener('click', function(e) {
                    e.stopPropagation(); // Prevent triggering the parent's onclick
                });
            }
        });
    }

    // Simplified message dismissal function
    function initMessageDismissal() {
        // Track active messages
        window.activeMessages = [];

        // Function to dismiss a message
        window.dismissMessage = function(message) {
            message.style.opacity = '0';
            message.style.transform = 'translate(-50%, -20px)';

            setTimeout(() => {
                if (message.parentNode) {
                    message.parentNode.removeChild(message);
                }

                // Remove from active messages
                const index = window.activeMessages.indexOf(message);
                if (index > -1) {
                    window.activeMessages.splice(index, 1);
                }
            }, 400);
        };

        // Simplified notification function that doesn't log activities
        window.showNotification = function(message, type = 'info') {
            // Skip all initialization messages
            if (message.includes('initialized') || message.includes('Activity logging')) {
                return null;
            }

            const notificationElement = document.createElement('div');
            notificationElement.className = `message ${type}`;
            notificationElement.textContent = message;

            const closeBtn = document.createElement('button');
            closeBtn.className = 'message-close';
            closeBtn.innerHTML = '&times;';
            closeBtn.setAttribute('aria-label', 'Dismiss message');

            notificationElement.appendChild(closeBtn);
            document.body.appendChild(notificationElement);

            closeBtn.addEventListener('click', function() {
                window.dismissMessage(notificationElement);
            });

            // Auto-dismiss all messages
            setTimeout(() => {
                window.dismissMessage(notificationElement);
            }, 3000);

            return notificationElement;
        };

        // Empty stub for logUserActivity that does nothing
        window.logUserActivity = function() {
            // Do nothing - completely disabled
            return;
        };
    }

    // Simplified retro effects
    function addRetroEffects() {
        const buttons = document.querySelectorAll('.button');
        buttons.forEach(button => {
            button.addEventListener('mouseenter', function() {
                this.style.transform = 'translateY(-2px)';
                this.style.transition = 'all 0.5s ease';
                this.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.1)';
            });

            button.addEventListener('mouseleave', function() {
                this.style.transform = '';
                this.style.transition = 'all 0.5s ease';
                this.style.boxShadow = '';
            });
        });
    }
    </script>

    <!-- Lightbox functionality -->
    <script>
        function openLightbox(imageId, imageTitle) {
            const lightbox = document.getElementById('imageLightbox');
            const lightboxImage = document.getElementById('lightboxImage');
            const lightboxTitle = document.getElementById('lightboxTitle');

            lightboxImage.src = '${pageContext.request.contextPath}/imagedisplay?id=' + imageId;
            lightboxTitle.textContent = imageTitle;

            lightbox.classList.add('active');
            document.body.style.overflow = 'hidden'; // Prevent scrolling when lightbox is open
        }

        function closeLightbox() {
            const lightbox = document.getElementById('imageLightbox');
            lightbox.classList.remove('active');
            document.body.style.overflow = ''; // Restore scrolling
        }

        // Close lightbox when clicking outside the image
        document.getElementById('imageLightbox').addEventListener('click', function(e) {
            if (e.target === this) {
                closeLightbox();
            }
        });

        // Close lightbox with Escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape' && document.getElementById('imageLightbox').classList.contains('active')) {
                closeLightbox();
            }
        });
    </script>
</body>
</html>
