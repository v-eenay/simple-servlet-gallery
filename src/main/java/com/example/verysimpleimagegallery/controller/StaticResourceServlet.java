package com.example.verysimpleimagegallery.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servlet to handle static resources like CSS, JS, and images.
 * This ensures that static resources are properly served even if there are issues with the default resource handling.
 */
@WebServlet(name = "StaticResourceServlet", urlPatterns = {"/static/*"})
public class StaticResourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Remove leading slash
        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        
        // Determine content type based on file extension
        String contentType = getServletContext().getMimeType(pathInfo);
        if (contentType == null) {
            // Default to text/plain if content type cannot be determined
            contentType = "text/plain";
        }
        
        // Set content type
        response.setContentType(contentType);
        
        // Try to find the resource
        try (InputStream in = getServletContext().getResourceAsStream("/assets/" + pathInfo)) {
            if (in == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            // Copy the resource to the response output stream
            try (OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
