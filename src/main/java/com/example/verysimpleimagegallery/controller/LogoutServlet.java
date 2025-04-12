package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.ActivityLogService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current session
        HttpSession session = request.getSession(false);

        // Log the logout activity if user is logged in
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                // Log the logout activity
                String userType = "User";
                if (user.isAdmin()) userType = "Admin";
                if (user.isSuperAdmin()) userType = "Super Admin";

                ActivityLogService.logActivity(
                    userType + " logout: " + user.getEmail(),
                    "login",
                    user.getId()
                );
            }

            // Invalidate the session
            session.removeAttribute("user");
            session.removeAttribute("isLoggedIn");
            session.invalidate();
        }

        // Redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward POST requests to doGet method
        doGet(request, response);
    }
}