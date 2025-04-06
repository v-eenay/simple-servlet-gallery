/**
 * Elegant Image Gallery - Main JavaScript
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
});

/**
 * Message notification system
 * Auto-hides messages after a delay
 */
function initMessageSystem() {
    const messages = document.querySelectorAll('.message');
    
    messages.forEach(message => {
        // Add slide-in animation
        message.style.animation = 'slideInDown 0.5s ease forwards';
        
        // Auto-hide after delay
        setTimeout(() => {
            message.style.opacity = '0';
            message.style.transform = 'translateY(-10px)';
            setTimeout(() => message.remove(), 500);
        }, 5000);
    });
}

/**
 * Form validation with elegant error handling
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
            });
            
            input.addEventListener('blur', () => {
                input.parentElement.classList.remove('focused');
                
                // Validate on blur
                if (input.hasAttribute('required') && !input.value.trim()) {
                    markFieldAsError(input, 'This field is required');
                } else if (input.type === 'email' && input.value.trim()) {
                    validateEmail(input);
                } else {
                    clearFieldError(input);
                }
            });
        });
        
        // Form submission validation
        form.addEventListener('submit', (e) => {
            const requiredFields = form.querySelectorAll('input[required], textarea[required], select[required]');
            let isValid = true;
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    markFieldAsError(field, 'This field is required');
                } else if (field.type === 'email' && field.value.trim()) {
                    if (!validateEmail(field)) {
                        isValid = false;
                    }
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                
                // Scroll to first error with smooth animation
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
 * @param {HTMLInputElement} field - Email input field
 * @returns {boolean} - True if valid
 */
function validateEmail(field) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (!emailPattern.test(field.value.trim())) {
        markFieldAsError(field, 'Please enter a valid email address');
        return false;
    } else {
        clearFieldError(field);
        return true;
    }
}

/**
 * Marks a form field as having an error
 * @param {HTMLElement} field - The form field
 * @param {string} message - Error message
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
    
    // Add shake animation
    field.style.animation = 'shake 0.5s ease';
    setTimeout(() => {
        field.style.animation = '';
    }, 500);
}

/**
 * Clears error state from a field
 * @param {HTMLElement} field - The form field
 */
function clearFieldError(field) {
    field.classList.remove('error');
    const errorDiv = field.parentNode.querySelector('.field-error');
    if (errorDiv) {
        errorDiv.remove();
    }
}

/**
 * Gallery animations
 * Adds fade-in, hover effects and loading animations
 */
function initGalleryAnimations() {
    // Image loading animations
    const galleryImages = document.querySelectorAll('.gallery-item img');
    
    galleryImages.forEach(img => {
        // Show loading state initially
        img.style.opacity = '0';
        
        // When image loads
        img.addEventListener('load', () => {
            img.classList.add('loaded');
        });
        
        // If image is already loaded
        if (img.complete) {
            img.classList.add('loaded');
        }
    });
    
    // Gallery item stagger effect
    const galleryItems = document.querySelectorAll('.gallery-item');
    galleryItems.forEach((item, index) => {
        item.style.animation = `fadeIn 0.5s ease forwards ${index * 0.1}s`;
    });
}

/**
 * Scroll to top functionality
 * Shows/hides button based on scroll position
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
 * Page transition effects
 */
function initPageTransitions() {
    // Add page transition class to body
    document.body.classList.add('page-transition');
    
    // Add fade-in animation to main content
    const mainContent = document.querySelector('main') || document.body;
    mainContent.style.animation = 'fadeIn 0.8s ease-out';
    
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
            
            // Fade out animation
            document.body.style.animation = 'fadeOut 0.3s ease forwards';
            
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
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes fadeOut {
        from { opacity: 1; }
        to { opacity: 0; }
    }
    
    @keyframes slideInDown {
        from { opacity: 0; transform: translateY(-20px); }
        to { opacity: 1; transform: translateY(0); }
    }
    
    @keyframes shake {
        0%, 100% { transform: translateX(0); }
        20%, 60% { transform: translateX(-5px); }
        40%, 80% { transform: translateX(5px); }
    }
    
    .page-transition {
        transition: opacity 0.3s ease;
    }
    
    .focused {
        position: relative;
    }
    
    .focused::after {
        content: '';
        position: absolute;
        bottom: -3px;
        left: 0;
        width: 100%;
        height: 2px;
        background-color: var(--primary);
        transform: scaleX(0);
        transition: transform 0.3s ease;
    }
    
    .focused.active::after {
        transform: scaleX(1);
    }
`;
document.head.appendChild(styleSheet);