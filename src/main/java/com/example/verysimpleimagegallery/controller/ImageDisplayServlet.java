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
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            GalleryItem galleryItem = GalleryService.getGalleryItemById(id);

            if (galleryItem != null && galleryItem.getImage() != null) {
                byte[] image = galleryItem.getImage();

                // Optional: infer content type from title
                String title = galleryItem.getTitle().toLowerCase();
                if (title.endsWith(".png")) {
                    response.setContentType("image/png");
                } else if (title.endsWith(".gif")) {
                    response.setContentType("image/gif");
                } else {
                    response.setContentType("image/jpeg");
                }

                response.setContentLength(image.length);
                response.getOutputStream().write(image);
                response.getOutputStream().flush();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image ID");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}