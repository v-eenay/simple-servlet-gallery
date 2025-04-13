package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.Tag;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.ActivityLogService;
import com.example.verysimpleimagegallery.service.GalleryService;
import com.example.verysimpleimagegallery.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Servlet for editing existing gallery items.
 * Handles both displaying the edit form and processing the edit submission.
 */
@WebServlet(name = "EditImageServlet", value = "/editimage")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 1024 * 1024 * 10,       // 10 MB
        maxRequestSize = 1024 * 1024 * 15     // 15 MB
)
public class EditImageServlet extends HttpServlet {

    /**
     * Handles GET requests to display the edit form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get the image ID from the request
        String imageIdParam = request.getParameter("id");
        if (imageIdParam == null || imageIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/viewgallery?error=No image specified");
            return;
        }

        try {
            int imageId = Integer.parseInt(imageIdParam);
            GalleryItem item = GalleryService.getGalleryItemById(imageId);

            // Check if the image exists and belongs to the user
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/viewgallery?error=Image not found");
                return;
            }

            if (item.getUserId() != user.getId() && user.getRole() != 0) {
                response.sendRedirect(request.getContextPath() + "/viewgallery?error=You do not have permission to edit this image");
                return;
            }

            // Get all available tags for the dropdown
            List<Tag> allTags = TagService.getAllTags();
            request.setAttribute("allTags", allTags);
            
            // Set the gallery item in the request
            request.setAttribute("galleryItem", item);

            // Forward to the edit form
            request.getRequestDispatcher("/WEB-INF/view/protected/edit-image.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/viewgallery?error=Invalid image ID");
        }
    }

    /**
     * Handles POST requests to process the edit form submission.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get the image ID from the request
        String imageIdParam = request.getParameter("id");
        if (imageIdParam == null || imageIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/viewgallery?error=No image specified");
            return;
        }

        try {
            int imageId = Integer.parseInt(imageIdParam);
            GalleryItem item = GalleryService.getGalleryItemById(imageId);

            // Check if the image exists and belongs to the user
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/viewgallery?error=Image not found");
                return;
            }

            if (item.getUserId() != user.getId() && user.getRole() != 0) {
                response.sendRedirect(request.getContextPath() + "/viewgallery?error=You do not have permission to edit this image");
                return;
            }

            // Get the updated title
            String title = request.getParameter("title");
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("error", "Title is required");
                request.setAttribute("galleryItem", item);
                request.getRequestDispatcher("/WEB-INF/view/protected/edit-image.jsp").forward(request, response);
                return;
            }

            // Get the updated tags
            String tags = request.getParameter("tags");

            // Check if a new image was uploaded
            Part filePart = request.getPart("image");
            byte[] imageData = null;
            if (filePart != null && filePart.getSize() > 0) {
                // Read the image data
                try (InputStream inputStream = filePart.getInputStream()) {
                    imageData = inputStream.readAllBytes();
                }
            }

            // Update the gallery item
            boolean success = GalleryService.updateGalleryItem(imageId, title, imageData, tags);

            if (success) {
                // Log the activity
                ActivityLogService.logActivity(
                        "Updated image: " + title,
                        "edit",
                        user.getId()
                );

                response.sendRedirect(request.getContextPath() + "/viewgallery?success=Image updated successfully");
            } else {
                request.setAttribute("error", "Failed to update image");
                request.setAttribute("galleryItem", item);
                request.getRequestDispatcher("/WEB-INF/view/protected/edit-image.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/viewgallery?error=Invalid image ID");
        }
    }
}
