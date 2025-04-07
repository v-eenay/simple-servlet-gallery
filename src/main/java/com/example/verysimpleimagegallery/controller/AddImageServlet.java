package com.example.verysimpleimagegallery.controller;

import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.GalleryService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "AddImageServlet", value = "/addimage")
@MultipartConfig(
        fileSizeThreshold = 1024*1024*2,
        maxFileSize = 1024*1024*10,
        maxRequestSize = 1024*1024*50
)
public class AddImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        session.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/view/protected/add-item.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        String title = request.getParameter("title");
        byte[] imageBytes = null;
        Part imagePart = request.getPart("image");
        if (imagePart != null) {
            imageBytes = imagePart.getInputStream().readAllBytes();
        }
        GalleryItem item = new GalleryItem();
        item.setTitle(title);
        item.setImage(imageBytes);
        item.setUserId(user.getId());
        if (GalleryService.addGallery(item)>0){
            session.setAttribute("user", user);
            request.setAttribute("imgmsg","You have successfully added a new image");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        else{
            request.setAttribute("imgmsg","Something went wrong");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}