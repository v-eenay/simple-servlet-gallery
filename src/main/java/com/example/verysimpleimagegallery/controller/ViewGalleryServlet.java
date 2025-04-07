package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.GalleryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet responsible for displaying the user's gallery and recent activities.
 * Handles both personal gallery items and system-wide recent uploads.
 */
@WebServlet(name = "ViewGalleryServlet", value = "/viewgallery")
public class ViewGalleryServlet extends HttpServlet {
    
    private static final int RECENT_ACTIVITIES_LIMIT = 5;

    /**
     * Handles GET requests to view the gallery.
     * Retrieves and displays user's gallery items and recent activities.
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

        // Get user's gallery items
        ArrayList<GalleryItem> galleryItems = GalleryService.getGalleryItem(user.getId());
        request.setAttribute("galleryItems", galleryItems);

        // Get recent activities across all users
        ArrayList<GalleryItem> recentActivities = GalleryService.getRecentActivities(RECENT_ACTIVITIES_LIMIT);
        request.setAttribute("recentActivities", recentActivities);

        // Forward to gallery view
        request.getRequestDispatcher("/WEB-INF/view/protected/gallery.jsp")
              .forward(request, response);
    }

    /**
     * POST method is not used for this servlet.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}