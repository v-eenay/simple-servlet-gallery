package com.example.verysimpleimagegallery.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // don't create if not exists

        // Safe login check
        boolean isLoggedIn = session != null && Boolean.TRUE.equals(session.getAttribute("isLoggedIn"));

        String uri = req.getRequestURI();
        String ctx = req.getContextPath();

        // Allow login/logout/register and static files
        boolean isPublic = uri.equals(ctx + "/login") || uri.equals(ctx + "/logout") || uri.equals(ctx + "/register") ||
                uri.contains("/assets/") || uri.contains("/static/") ||
                uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg") ||
                uri.endsWith(".jpeg") || uri.endsWith(".gif") || uri.endsWith(".svg") || uri.endsWith(".ico");

        if (isLoggedIn || isPublic) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(ctx + "/login");
        }
    }
}
