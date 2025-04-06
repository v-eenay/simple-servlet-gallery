package com.example.verysimpleimagegallery.filter;

import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AdminAuthFilter", urlPatterns = {"/admin/*"})
public class AdminAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        
        // Check if user is logged in and has admin access
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            
            if (AuthService.hasAdminAccess(user)) {
                // User has admin access, continue with the request
                chain.doFilter(request, response);
                return;
            }
        }
        
        // Not authorized, redirect to home page with error
        httpRequest.setAttribute("error", "You do not have permission to access admin pages.");
        httpRequest.getRequestDispatcher("/").forward(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
} 