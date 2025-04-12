package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.service.ActivityLogService;
import com.example.verysimpleimagegallery.service.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/register.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Use AuthService to create user with hashed password
            int userId = AuthService.createUser(fullname, email, password);

            if (userId > 0) {
                // Log the registration activity
                ActivityLogService.logActivity(
                    "New user registered: " + fullname + " (" + email + ")",
                    "register",
                    userId
                );

                // Registration successful, redirect to login page
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/login.jsp?regerror=false");
                dispatcher.forward(request, response);
            } else {
                // Registration failed (likely email already exists)
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/register.jsp?regerror=true");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            // Handle database error
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/register.jsp?regerror=true");
            dispatcher.forward(request, response);
        }
    }
}