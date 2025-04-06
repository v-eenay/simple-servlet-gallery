package com.example.verysimpleimagegallery.controller.admin;

import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AddAdminServlet", urlPatterns = {"/admin/add-admin"})
public class AddAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Only super admins can access this page
        if (!AuthService.hasSuperAdminAccess(currentUser)) {
            request.setAttribute("error", "Only super admins can add new admins.");
            request.getRequestDispatcher("/admin/dashboard").forward(request, response);
            return;
        }
        
        // Forward to add-admin JSP
        request.getRequestDispatcher("/WEB-INF/view/admin/add-admin.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Only super admins can add new admins
        if (!AuthService.hasSuperAdminAccess(currentUser)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard?error=unauthorized");
            return;
        }
        
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        boolean isSuperAdmin = request.getParameter("superAdmin") != null;
        
        // Validate inputs
        if (fullName == null || email == null || password == null || confirmPassword == null ||
                fullName.trim().isEmpty() || email.trim().isEmpty() || 
                password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            
            response.sendRedirect(request.getContextPath() + "/admin/add-admin?error=true");
            return;
        }
        
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            response.sendRedirect(request.getContextPath() + "/admin/add-admin?error=true");
            return;
        }
        
        try {
            int userId = AuthService.createAdminUser(fullName, email, password, isSuperAdmin);
            
            if (userId != -1) {
                response.sendRedirect(request.getContextPath() + "/admin/list-users?message=Admin added successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/add-admin?error=true");
            }
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/admin/add-admin?error=true");
        }
    }
} 