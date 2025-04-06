package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.service.GalleryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ImageDisplayServlet", value = "/imagedisplay")
public class ImageDisplayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        GalleryItem galleryItem = GalleryService.getGalleryItemById(id);
        byte[] image = null;
        assert galleryItem != null;
        image = galleryItem.getImage();
        if (image != null) {
            response.setContentType("image/jpeg");
            response.getOutputStream().write(image);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}