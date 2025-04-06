package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.GalleryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteImageServlet", value = "/deleteimage")
public class DeleteImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String imageIdStr = request.getParameter("id");
        
        if (imageIdStr == null || imageIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/gallery?error=true");
            return;
        }
        
        try {
            int imageId = Integer.parseInt(imageIdStr);
            GalleryItem item = GalleryService.getGalleryItemById(imageId);
            
            if (item == null || item.getUserId() != user.getId()) {
                response.sendRedirect(request.getContextPath() + "/viewgallery?error=true");
                return;
            }
            
            boolean deleted = GalleryService.deleteGalleryItem(item);
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/viewgallery?deleted=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/viewgallery?error=true");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/viewgallery?error=true");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}