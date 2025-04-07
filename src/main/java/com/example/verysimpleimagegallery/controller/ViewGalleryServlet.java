package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.GalleryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ViewGalleryServlet", value = "/viewgallery")
public class ViewGalleryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ArrayList<GalleryItem> galleryItems = GalleryService.getGalleryItem(user.getId());
        ArrayList<GalleryItem> recentActivities = GalleryService.getRecentActivities(5); // Get 5 most recent activities
        request.setAttribute("galleryItems", galleryItems);
        request.setAttribute("recentActivities", recentActivities);
        request.getRequestDispatcher("/WEB-INF/view/protected/gallery.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}