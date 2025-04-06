package com.example.verysimpleimagegallery.controller.admin;

import com.example.verysimpleimagegallery.dao.UserDAO;
import com.example.verysimpleimagegallery.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get counts for dashboard stats
        int totalUsers = UserDAO.getUserCount();
        int adminCount = UserDAO.getAdminCount();
        
        // Set attributes to be used in the JSP
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("adminCount", adminCount);
        
        // Forward to dashboard page
        request.getRequestDispatcher("/WEB-INF/view/admin/admin-dashboard.jsp").forward(request, response);
    }
} 