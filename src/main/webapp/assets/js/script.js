/**
 * Minimal Image Gallery - Main JavaScript
 * Handles animations, form validation, and UI interactions
 */

document.addEventListener('DOMContentLoaded', () => {
    // Initialize all components
    initMessageSystem();
    initFormValidation();
    initGalleryAnimations();
    initScrollToTop();
    initSmoothScrolling();
    initPageTransitions();
    enhanceInputFields();
});

/**
 * Message notification system
 */
function initMessageSystem() {
    const messages = document.querySelectorAll('.message');
    
    messages.forEach(message => {
        // Slide in from top with fade
        message.style.animation = 'fadeInDown 0.5s ease forwards';
        
        // Auto-hide after delay
        setTimeout(() => {
            message.style.opacity = '0';
            setTimeout(() => message.remove(), 400);
        }, 4000);
    });
}

/**
 * Form validation with minimal styling
 */
function initFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        // Add input focus effects
        const inputs = form.querySelectorAll('input, textarea, select');
        inputs.forEach(input => {
            // Focus effect
            input.addEventListener('focus', () => {
                input.parentElement.classList.add('focused');
                input.parentElement.classList.add('active');
            });
            
            input.addEventListener('blur', () => {
                input.parentElement.classList.remove('active');
                
                // Keep focused class if has value
                if (input.value.trim() === '') {
                    input.parentElement.classList.remove('focused');
                }
                
                // Validate on blur
                if (input.hasAttribute('required') && !input.value.trim()) {
                    markFieldAsError(input, 'Required');
                } else if (input.type === 'email' && input.value.trim()) {
                    validateEmail(input);
                } else {
                    clearFieldError(input);
                }
            });
            
            // Add filled class if field already has a value
            if (input.value.trim() !== '') {
                input.parentElement.classList.add('focused');
            }
        });
        
        // Form submission validation
        form.addEventListener('submit', (e) => {
            const requiredFields = form.querySelectorAll('input[required], textarea[required], select[required]');
            let isValid = true;
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    markFieldAsError(field, 'Required');
                } else if (field.type === 'email' && field.value.trim()) {
                    if (!validateEmail(field)) {
                        isValid = false;
                    }
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                
                // Scroll to first error
                const firstError = form.querySelector('.error');
                if (firstError) {
                    firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    firstError.focus();
                }
            }
        });
    });
}

/**
 * Validates email format
 */
function validateEmail(field) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (!emailPattern.test(field.value.trim())) {
        markFieldAsError(field, 'Invalid email');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

/**
 * Marks a form field as having an error
 */
function markFieldAsError(field, message) {
    field.classList.add('error');
    
    // Remove existing error message if any
    clearFieldError(field);
    
    // Create error message element
    const errorDiv = document.createElement('div');
    errorDiv.classList.add('field-error');
    errorDiv.textContent = message;
    
    // Insert after the field
    field.parentNode.appendChild(errorDiv);
}

/**
 * Clears error state from a field
 */
function clearFieldError(field) {
    field.classList.remove('error');
    const errorDiv = field.parentNode.querySelector('.field-error');
    if (errorDiv) {
        errorDiv.remove();
    }
}

/**
 * Enhance input fields with minimal styling
 */
function enhanceInputFields() {
    // Add floating labels effect
    document.querySelectorAll('label').forEach(label => {
        const input = label.querySelector('input, textarea, select');
        if (!input) return;
        
        // Extract text content (excluding the input element)
        const labelText = label.childNodes[0]?.nodeValue?.trim();
        if (!labelText) return;
        
        // Clear the text node
        label.childNodes[0].nodeValue = '';
        
        // Create span for label text
        const labelSpan = document.createElement('span');
        labelSpan.textContent = labelText;
        labelSpan.classList.add('label-text');
        
        // Insert at beginning of label
        label.insertBefore(labelSpan, label.firstChild);
    });

    // Initialize file upload functionality
    initFileUpload();
}

/**
 * Handle file upload with preview
 */
function initFileUpload() {
    const uploadZone = document.querySelector('.upload-zone');
    const imageInput = document.getElementById('imageInput');
    const previewContainer = document.querySelector('.preview-container');
    const imagePreview = document.getElementById('imagePreview');
    const removePreviewBtn = document.querySelector('.remove-preview');
    const uploadPrompt = document.querySelector('.upload-prompt');

    if (!uploadZone || !imageInput) return;

    // Handle drag and drop events
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        uploadZone.addEventListener(eventName, preventDefaults, false);
    });

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    // Handle drag enter/over
    ['dragenter', 'dragover'].forEach(eventName => {
        uploadZone.addEventListener(eventName, () => {
            uploadZone.classList.add('dragover');
        });
    });

    // Handle drag leave/drop
    ['dragleave', 'drop'].forEach(eventName => {
        uploadZone.addEventListener(eventName, () => {
            uploadZone.classList.remove('dragover');
        });
    });

    // Handle dropped files
    uploadZone.addEventListener('drop', (e) => {
        const dt = e.dataTransfer;
        const files = dt.files;

        if (files.length > 0) {
            handleFiles(files);
        }
    });

    // Handle file input change
    imageInput.addEventListener('change', (e) => {
        if (e.target.files.length > 0) {
            handleFiles(e.target.files);
        }
    });

    // Handle file selection
    function handleFiles(files) {
        const file = files[0];
        
        // Validate file type
        if (!file.type.startsWith('image/')) {
            alert('Please upload an image file');
            return;
        }

        // Validate file size (5MB)
        if (file.size > 5 * 1024 * 1024) {
            alert('File size must be less than 5MB');
            return;
        }

        // Show preview
        const reader = new FileReader();
        reader.onload = (e) => {
            imagePreview.src = e.target.result;
            uploadPrompt.style.display = 'none';
            previewContainer.style.display = 'block';
        };
        reader.readAsDataURL(file);
    }

    // Remove preview
    if (removePreviewBtn) {
        removePreviewBtn.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation(); // Prevent event from bubbling up to upload zone
            imageInput.value = '';
            imagePreview.src = '#';
            previewContainer.style.display = 'none';
            uploadPrompt.style.display = 'flex';
        });
    }
    
    // Click on upload zone should trigger file input
    uploadZone.addEventListener('click', () => {
        imageInput.click();
    });
}

/**
 * Gallery animations and better image display
 */
function initGalleryAnimations() {
    // Image loading animations with lazy loading
    const galleryImages = document.querySelectorAll('.gallery-item img');
    
    if (galleryImages.length === 0) return;
    
    // Check if IntersectionObserver is supported
    if ('IntersectionObserver' in window) {
        // Set up intersection observer for lazy loading
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    const src = img.getAttribute('src');
                    
                    // Skip placeholder images and set real src
                    if (src && src.includes('data:image')) {
                        // Get the original path from the alt attribute or data-src
                        const originalSrc = img.dataset.src || img.getAttribute('alt');
                        if (originalSrc) {
                            img.src = originalSrc;
                        }
                    }
                    
                    // When image loads
                    img.onload = () => {
                        img.classList.add('loaded');
                    };
                    
                    observer.unobserve(img);
                }
            });
        }, { rootMargin: '50px 0px' });
        
        galleryImages.forEach(img => {
            imageObserver.observe(img);
        });
    } else {
        // Fallback for browsers that don't support IntersectionObserver
        galleryImages.forEach(img => {
            img.classList.add('loaded');
        });
    }
    
    // Stagger gallery items appearance
    const galleryItems = document.querySelectorAll('.gallery-item');
    
    if ('IntersectionObserver' in window) {
        const itemObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach((entry, index) => {
                if (entry.isIntersecting) {
                    // Delay based on index for staggered effect
                    setTimeout(() => {
                        entry.target.classList.add('visible');
                    }, index * 100);
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.1 });
        
        galleryItems.forEach(item => {
            item.style.opacity = '0';
            item.style.transform = 'translateY(10px)';
            item.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            itemObserver.observe(item);
        });
    } else {
        // Fallback for browsers that don't support IntersectionObserver
        galleryItems.forEach(item => {
            item.classList.add('visible');
        });
    }
}

/**
 * Scroll to top functionality
 */
function initScrollToTop() {
    const scrollBtn = document.createElement('button');
    scrollBtn.classList.add('scroll-top');
    scrollBtn.innerHTML = '↑';
    scrollBtn.setAttribute('aria-label', 'Scroll to top');
    document.body.appendChild(scrollBtn);
    
    // Show/hide based on scroll position
    window.addEventListener('scroll', () => {
        if (window.pageYOffset > 300) {
            scrollBtn.classList.add('visible');
        } else {
            scrollBtn.classList.remove('visible');
        }
    });
    
    // Smooth scroll to top
    scrollBtn.addEventListener('click', () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
}

/**
 * Smooth scrolling for all anchor links
 */
function initSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            if (targetId === '#') return;
            
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                targetElement.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Minimal page transition effects
 */
function initPageTransitions() {
    // Add fade-in animation to main content
    document.body.style.opacity = '0';
    document.body.style.transition = 'opacity 0.5s ease';
    
    // Fade in body when page loads
    setTimeout(() => {
        document.body.style.opacity = '1';
    }, 50);
    
    // Set up exit animations for links
    document.querySelectorAll('a:not([href^="#"])').forEach(link => {
        link.addEventListener('click', function(e) {
            // Skip for external links or links with target attribute
            if (
                link.getAttribute('target') || 
                link.getAttribute('href').startsWith('http') ||
                link.getAttribute('href').includes('javascript:')
            ) {
                return;
            }
            
            e.preventDefault();
            const href = this.getAttribute('href');
            
            // Fade out
            document.body.style.opacity = '0';
            
            // Navigate after animation completes
            setTimeout(() => {
                window.location.href = href;
            }, 300);
        });
    });
}

// Define CSS animations in JS for consistency
const styleSheet = document.createElement('style');
styleSheet.type = 'text/css';
styleSheet.textContent = `
    @keyframes fadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
    }
    
    @keyframes fadeInDown {
        from { opacity: 0; transform: translateY(-10px); }
        to { opacity: 1; transform: translateY(0); }
    }
    
    .gallery-item.visible {
        opacity: 1 !important;
        transform: translateY(0) !important;
    }
    
    .focused .label-text {
        transform: translateY(-1.5rem) scale(0.85);
        color: var(--text);
        font-weight: 500;
    }
    
    .label-text {
        position: absolute;
        left: 0;
        top: 0.35rem;
        transition: transform 0.25s ease, color 0.25s ease;
        transform-origin: left top;
        pointer-events: none;
    }
    
    .active.focused::after {
        transform: scaleX(1);
    }
    
    label.focused::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        height: 1px;
        background-color: var(--text);
        transform: scaleX(0);
        transition: transform 0.25s ease;
    }
`;
document.head.appendChild(styleSheet);