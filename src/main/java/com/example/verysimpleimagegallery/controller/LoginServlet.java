package com.example.verysimpleimagegallery.controller;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.AuthService;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = null;
        
        try {
            // Use AuthService to validate user with secure password checking
            user = AuthService.validateUser(email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        if (user != null) {
            // Login successful
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("isLoggedIn", true);
            if(user.isRegularUser()){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp?loginerror=false");
                dispatcher.forward(request, response);
            }
            else if (user.isAdmin() || user.isSuperAdmin()) {
                // Use sendRedirect instead of forward for admin dashboard
                // This ensures a new request is made and AdminDashboardServlet properly loads all data
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?loginerror=false");
            }
        } else {
            // Login failed
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/login.jsp?error=true");
            dispatcher.forward(request, response);
        }
    }
}