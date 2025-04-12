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
 * Servlet responsible for displaying the home page with recent activities.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/"})
public class HomeServlet extends HttpServlet {

    private static final int RECENT_ACTIVITIES_LIMIT = 8;

    /**
     * Handles GET requests to view the home page.
     * Retrieves and displays recent activities across all users.
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

        // Get recent activities across all users
        ArrayList<GalleryItem> recentActivities = GalleryService.getRecentActivities(RECENT_ACTIVITIES_LIMIT);
        request.setAttribute("recentActivities", recentActivities);

        // Check for success or error messages from redirects
        String success = request.getParameter("success");
        String error = request.getParameter("error");

        if (success != null && success.equals("true")) {
            request.setAttribute("imgmsg", "You have successfully added a new image");
        } else if (error != null && error.equals("true")) {
            request.setAttribute("errormsg", "Something went wrong with your request");
        }

        // Forward to home view
        request.getRequestDispatcher("/index.jsp")
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
