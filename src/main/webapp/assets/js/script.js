document.addEventListener('DOMContentLoaded', function() {
    initImageUpload();
    initGalleryAnimations();
    initMessageDismissal();
    addRetroEffects();
});

function initImageUpload() {
    const imageInput = document.getElementById('imageInput');
    const uploadZone = document.querySelector('.upload-zone');
    const previewContainer = document.querySelector('.preview-container');
    const imagePreview = document.getElementById('imagePreview');
    const removePreviewBtn = document.querySelector('.remove-preview');

    if (!imageInput) return;
    if (uploadZone) {
        uploadZone.addEventListener('click', function() {
            imageInput.click();
        });
    }


    if (uploadZone) {
        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            uploadZone.addEventListener(eventName, preventDefaults, false);
        });

        function preventDefaults(e) {
            e.preventDefault();
            e.stopPropagation();
        }

        ['dragenter', 'dragover'].forEach(eventName => {
            uploadZone.addEventListener(eventName, highlight, false);
        });

        ['dragleave', 'drop'].forEach(eventName => {
            uploadZone.addEventListener(eventName, unhighlight, false);
        });

        function highlight() {
            uploadZone.classList.add('highlight');
            uploadZone.style.transition = 'all 0.3s ease';
        }

        function unhighlight() {
            uploadZone.classList.remove('highlight');
            uploadZone.style.transition = 'all 0.3s ease';
        }

        uploadZone.addEventListener('drop', handleDrop, false);

        function handleDrop(e) {
            const dt = e.dataTransfer;
            const files = dt.files;
            if (files.length) {
                imageInput.files = files;
                updateImagePreview();
            }
        }
    }

    if (imageInput) {
        imageInput.addEventListener('change', updateImagePreview);
    }
    function updateImagePreview() {
        if (!imageInput.files || !imageInput.files[0]) return;

        const file = imageInput.files[0];


        if (!file.type.match('image.*')) {
            alert('Please select an image file (jpg, png, gif)');
            return;
        }


        if (file.size > 5 * 1024 * 1024) {
            alert('File size exceeds 5MB limit');
            return;
        }

        const reader = new FileReader();

        reader.onload = function(e) {
            imagePreview.src = e.target.result;
            previewContainer.style.display = 'block';
            document.querySelector('.upload-prompt').style.display = 'none';
        };

        reader.readAsDataURL(file);
    }


    if (removePreviewBtn) {
        removePreviewBtn.addEventListener('click', function() {
            imagePreview.src = '#';
            previewContainer.style.display = 'none';
            document.querySelector('.upload-prompt').style.display = 'flex';
            imageInput.value = '';
        });
    }
}

function initGalleryAnimations() {
    const galleryItems = document.querySelectorAll('.gallery-item');

    if (galleryItems.length === 0) return;
    galleryItems.forEach(item => {
        const img = item.querySelector('img');

        img.addEventListener('click', function() {
            openLightbox(this.src, item.querySelector('.item-title').textContent);
        });

        item.addEventListener('mouseenter', function() {
            this.style.transform = 'scale(1.02)';
            this.style.transition = 'all 0.5s ease';
            img.style.filter = 'brightness(1.05)';
            img.style.transition = 'all 0.5s ease';
        });

        item.addEventListener('mouseleave', function() {
            this.style.transform = 'scale(1)';
            this.style.transition = 'all 0.5s ease';
            img.style.filter = 'brightness(1)';
            img.style.transition = 'all 0.5s ease';
        });
    });


    const activityList = document.querySelector('.activity-list');
    if (activityList) {
        // Ensure proper scrolling behavior for the activity list
        activityList.addEventListener('wheel', function(e) {
            // Using natural vertical scrolling
            // No need to prevent default or modify scroll behavior
        });

        // Add hover effects to activity items
        const activityItems = document.querySelectorAll('.activity-item');
        activityItems.forEach(item => {
            item.addEventListener('mouseenter', function() {
                this.style.transform = 'translateX(2px)';
                this.style.transition = 'all 0.3s ease';
                this.style.backgroundColor = 'rgba(0, 0, 0, 0.04)';
                this.style.borderLeft = '2px solid var(--color-accent)';
            });

            item.addEventListener('mouseleave', function() {
                this.style.transform = 'translateX(0)';
                this.style.transition = 'all 0.3s ease';
                this.style.backgroundColor = 'rgba(0, 0, 0, 0.02)';
                this.style.borderLeft = '2px solid transparent';
            });
        });
    }

    // Enhanced function to log user activity and display notification
    window.logUserActivity = function(activity, type = 'info') {
        // Always show notification to user
        if (window.showNotification) {
            window.showNotification(activity, type);
        } else {
            // Fallback if showNotification is not available
            console.log('Activity:', activity, 'Type:', type);
        }

        // Add to activity log if it exists
        const activityList = document.querySelector('.activity-list');
        if (activityList) {
            const activityItem = document.createElement('div');
            activityItem.className = 'activity-item';

            // Add type as a class for potential styling
            if (type) {
                activityItem.classList.add(`activity-${type}`);
            }

            const timeElement = document.createElement('div');
            timeElement.className = 'activity-time';

            // Format current time
            const now = new Date();
            const timeString = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
            timeElement.textContent = timeString;

            const contentElement = document.createElement('div');
            contentElement.className = 'activity-content';
            contentElement.textContent = activity;

            activityItem.appendChild(timeElement);
            activityItem.appendChild(contentElement);

            // Add to the top of the list
            if (activityList.firstChild) {
                activityList.insertBefore(activityItem, activityList.firstChild);
            } else {
                activityList.appendChild(activityItem);
            }

            // Add hover effects
            activityItem.addEventListener('mouseenter', function() {
                this.style.transform = 'translateX(2px)';
                this.style.transition = 'all 0.3s ease';
                this.style.backgroundColor = 'rgba(0, 0, 0, 0.04)';
                this.style.borderLeft = '2px solid var(--color-accent)';
            });

            activityItem.addEventListener('mouseleave', function() {
                this.style.transform = 'translateX(0)';
                this.style.transition = 'all 0.3s ease';
                this.style.backgroundColor = 'rgba(0, 0, 0, 0.02)';
                this.style.borderLeft = '2px solid transparent';
            });

            // Limit the number of activity items to prevent the list from getting too long
            const maxItems = 20;
            const items = activityList.querySelectorAll('.activity-item');
            if (items.length > maxItems) {
                for (let i = maxItems; i < items.length; i++) {
                    activityList.removeChild(items[i]);
                }
            }
        }
    };

    // We no longer need to show the initialization message
    // It was causing issues by covering the screen
    // The activity logging system will still work without this message
}

function openLightbox(src, title) {
    const lightbox = document.createElement('div');
    lightbox.className = 'lightbox';

    const lightboxContent = document.createElement('div');
    lightboxContent.className = 'lightbox-content';

    const lightboxImg = document.createElement('img');
    lightboxImg.src = src;
    lightboxImg.alt = title || 'Gallery Image';

    const lightboxTitle = document.createElement('div');
    lightboxTitle.className = 'lightbox-title';
    lightboxTitle.textContent = title || '';

    const closeBtn = document.createElement('button');
    closeBtn.className = 'lightbox-close';
    closeBtn.innerHTML = '&times;';
    closeBtn.setAttribute('aria-label', 'Close lightbox');


    lightboxContent.appendChild(lightboxImg);
    lightboxContent.appendChild(lightboxTitle);
    lightboxContent.appendChild(closeBtn);
    lightbox.appendChild(lightboxContent);
    document.body.appendChild(lightbox);


    document.body.style.overflow = 'hidden';


    setTimeout(() => {
        lightbox.classList.add('active');
    }, 10);


    closeBtn.addEventListener('click', closeLightbox);
    lightbox.addEventListener('click', function(e) {
        if (e.target === lightbox) {
            closeLightbox();
        }
    });


    document.addEventListener('keydown', function escapeHandler(e) {
        if (e.key === 'Escape') {
            closeLightbox();
            document.removeEventListener('keydown', escapeHandler);
        }
    });

    function closeLightbox() {
        lightbox.classList.remove('active');
        setTimeout(() => {
            document.body.removeChild(lightbox);
            document.body.style.overflow = '';
        }, 400);
    }
}

function initMessageDismissal() {
    // Track active messages to position them properly
    window.activeMessages = [];

    // Get all existing messages on the page
    const messages = document.querySelectorAll('.message');

    // If there are too many messages (more than 2), keep only the most recent ones
    if (messages.length > 2) {
        // Sort messages by their position in the DOM (most recent first)
        const sortedMessages = Array.from(messages).sort((a, b) => {
            // Compare their positions in the document
            return a.compareDocumentPosition(b) & Node.DOCUMENT_POSITION_PRECEDING ? 1 : -1;
        });

        // Keep only the first 2 messages, remove the rest
        sortedMessages.slice(2).forEach(msg => {
            if (msg.parentNode) {
                msg.parentNode.removeChild(msg);
            }
        });
    }

    // Process remaining messages
    const remainingMessages = document.querySelectorAll('.message');
    remainingMessages.forEach(message => {
        // Only add close button if it doesn't already exist
        if (!message.querySelector('.message-close')) {
            const closeBtn = document.createElement('button');
            closeBtn.className = 'message-close';
            closeBtn.innerHTML = '&times;';
            closeBtn.setAttribute('aria-label', 'Dismiss message');
            message.appendChild(closeBtn);

            closeBtn.addEventListener('click', function() {
                dismissMessage(message);
            });
        }

        // Make sure message is visible initially
        message.style.opacity = '1';
        message.style.display = 'block';

        // Add to active messages
        window.activeMessages.push(message);

        // Auto-dismiss success and info messages
        if (message.classList.contains('success') || message.classList.contains('info')) {
            const timeout = message.classList.contains('success') ? 5000 : 3000;
            setTimeout(() => {
                dismissMessage(message);
            }, timeout);
        }
    });

    // Update positions after processing all messages
    updateMessagePositions();

    // Function to dismiss a message and update positions
    function dismissMessage(message) {
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
                updateMessagePositions();
            }
        }, 400);
    }

    // Function to update message positions
    function updateMessagePositions() {
        let topOffset = 20;
        const spacing = 10;

        window.activeMessages.forEach((msg, index) => {
            if (index > 0) {
                const prevMsg = window.activeMessages[index - 1];
                const prevHeight = prevMsg.offsetHeight;
                topOffset += prevHeight + spacing;
            }

            msg.style.top = topOffset + 'px';
        });
    }

    // Enhanced notification function that can be called from anywhere
    window.showNotification = function(message, type = 'info') {
        const notificationElement = document.createElement('div');
        notificationElement.className = `message ${type}`;
        notificationElement.textContent = message;

        const closeBtn = document.createElement('button');
        closeBtn.className = 'message-close';
        closeBtn.innerHTML = '&times;';
        closeBtn.setAttribute('aria-label', 'Dismiss message');

        notificationElement.appendChild(closeBtn);
        document.body.appendChild(notificationElement);

        // Add to active messages
        window.activeMessages.push(notificationElement);
        updateMessagePositions();

        closeBtn.addEventListener('click', function() {
            dismissMessage(notificationElement);
        });

        // Auto-dismiss success and info messages
        if (type === 'success' || type === 'info') {
            const timeout = type === 'success' ? 5000 : 4000;
            setTimeout(() => {
                dismissMessage(notificationElement);
            }, timeout);
        }

        // Log to activity log if it exists
        if (window.logUserActivity && type !== 'error') {
            // Don't log errors to activity log to avoid duplication
            // since errors are typically logged separately
            window.logUserActivity(message, type);
        }

        return notificationElement;
    };
}

function addRetroEffects() {
    const grainOverlay = document.createElement('div');
    grainOverlay.className = 'grain-overlay';
    document.body.appendChild(grainOverlay);


    const buttons = document.querySelectorAll('.button');
    buttons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
            this.style.transition = 'all 0.5s ease';
            this.style.boxShadow = 'var(--box-shadow-hover)';
        });

        button.addEventListener('mouseleave', function() {
            this.style.transform = '';
            this.style.transition = 'all 0.5s ease';
            this.style.boxShadow = '';
        });
    });



    const style = document.createElement('style');
    style.textContent = `
        .lightbox {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.8);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 1000;
            opacity: 0;
            transition: opacity 0.4s ease;
        }

        .lightbox.active {
            opacity: 1;
        }

        .lightbox-content {
            position: relative;
            max-width: 90%;
            max-height: 90%;
            background-color: white;
            padding: 20px;
            border-radius: 4px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.5);
            transform: scale(0.9);
            transition: transform 0.4s ease;
        }

        .lightbox.active .lightbox-content {
            transform: scale(1);
        }

        .lightbox img {
            max-width: 100%;
            max-height: 70vh;
            display: block;
            margin: 0 auto;
        }

        .lightbox-title {
            margin-top: 10px;
            text-align: center;
            font-family: 'Space Mono', monospace;
            color: #232323;
        }

        .lightbox-close {
            position: absolute;
            top: -15px;
            right: -15px;
            width: 30px;
            height: 30px;
            background-color: #d4a373;
            color: white;
            border: none;
            border-radius: 50%;
            font-size: 20px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }

        .message {
            position: relative;
            padding-right: 30px;
        }

        .message-close {
            position: absolute;
            top: 10px;
            right: 10px;
            background: none;
            border: none;
            font-size: 18px;
            cursor: pointer;
            color: rgba(0, 0, 0, 0.5);
        }

        .grain-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            opacity: 0.03;
            z-index: -1;
            background-image: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIzMDAiIGhlaWdodD0iMzAwIj48ZmlsdGVyIGlkPSJhIiB4PSIwIiB5PSIwIj48ZmVUdXJidWxlbmNlIGJhc2VGcmVxdWVuY3k9Ii43NSIgc3RpdGNoVGlsZXM9InN0aXRjaCIgdHlwZT0iZnJhY3RhbE5vaXNlIi8+PGZlQ29sb3JNYXRyaXggdHlwZT0ic2F0dXJhdGUiIHZhbHVlcz0iMCIvPjwvZmlsdGVyPjxwYXRoIGQ9Ik0wIDBoMzAwdjMwMEgweiIgZmlsdGVyPSJ1cmwoI2EpIiBvcGFjaXR5PSIuMDUiLz48L3N2Zz4=');
        }

        .upload-zone.highlight {
            border-color: #d4a373;
            background-color: rgba(212, 163, 115, 0.1);
        }
    `;
    document.head.appendChild(style);
}