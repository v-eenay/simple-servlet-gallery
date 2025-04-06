package com.example.verysimpleimagegallery.controller.admin;

import com.example.verysimpleimagegallery.dao.UserDAO;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserListServlet", urlPatterns = {"/admin/list-users"})
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Get list of users based on current user's role
        List<User> users;
        if (AuthService.hasSuperAdminAccess(currentUser)) {
            // Super admin can see all users
            users = UserDAO.getAllUsers();
        } else {
            // Regular admin can only see non-admin users
            users = UserDAO.getNonAdminUsers();
        }
        
        request.setAttribute("users", users);
        request.setAttribute("currentUser", currentUser);
        
        // Forward to list-users JSP
        request.getRequestDispatcher("/WEB-INF/view/admin/list-users.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // This can be used for actions like filtering, etc.
        doGet(request, response);
    }
} 