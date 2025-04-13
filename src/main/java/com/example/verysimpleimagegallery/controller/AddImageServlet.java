package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.Tag;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.ActivityLogService;
import com.example.verysimpleimagegallery.service.GalleryService;
import com.example.verysimpleimagegallery.service.TagService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "AddImageServlet", value = "/addimage")
@MultipartConfig(
        fileSizeThreshold = 1024*1024*2,
        maxFileSize = 1024*1024*10,
        maxRequestSize = 1024*1024*50
)
public class AddImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get all available tags for the dropdown
        List<Tag> allTags = TagService.getAllTags();
        request.setAttribute("allTags", allTags);

        session.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/view/protected/add-item.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String title = request.getParameter("title");
        byte[] imageBytes = null;
        Part imagePart = request.getPart("image");
        if (imagePart != null) {
            imageBytes = imagePart.getInputStream().readAllBytes();
        }

        // Get tags from the request
        String tags = request.getParameter("tags");

        GalleryItem item = new GalleryItem();
        item.setTitle(title);
        item.setImage(imageBytes);
        item.setUserId(user.getId());
        int galleryItemId = GalleryService.addGallery(item);

        if (galleryItemId > 0){
            // Add tags to the image
            if (tags != null && !tags.trim().isEmpty()) {
                TagService.updateImageTags(galleryItemId, tags);
            }

            // Log the activity
            ActivityLogService.logActivity(
                "Image '" + title + "' uploaded",
                "upload",
                user.getId()
            );

            session.setAttribute("user", user);
            request.setAttribute("imgmsg","You have successfully added a new image");
            response.sendRedirect(request.getContextPath() + "/home?success=true");
        }
        else{
            response.sendRedirect(request.getContextPath() + "/home?error=true");
        }
    }
}