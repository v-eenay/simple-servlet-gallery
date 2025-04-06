package com.example.verysimpleimagegallery.controller.admin;

import com.example.verysimpleimagegallery.dao.GalleryItemDAO;
import com.example.verysimpleimagegallery.dao.UserDAO;
import com.example.verysimpleimagegallery.model.GalleryItem;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminGalleryServlet", urlPatterns = {"/admin/gallery"})
public class AdminGalleryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Only users with admin permissions can access this page
        if (!AuthService.hasAdminAccess(currentUser)) {
            response.sendRedirect(request.getContextPath() + "/?error=You do not have permission to access the admin gallery");
            return;
        }
        
        // Get all gallery items for admin users
        List<GalleryItem> galleryItems = GalleryItemDAO.getAllGalleryItems();
        
        // Set attributes for JSP
        request.setAttribute("galleryItems", galleryItems);
        request.setAttribute("currentUser", currentUser);
        
        // Forward to the admin gallery JSP
        request.getRequestDispatcher("/WEB-INF/view/admin/admin-gallery.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Only users with admin permissions can perform these actions
        if (!AuthService.hasAdminAccess(currentUser)) {
            response.sendRedirect(request.getContextPath() + "/?error=You do not have permission");
            return;
        }
        
        String action = request.getParameter("action");
        String itemIdStr = request.getParameter("itemId");
        
        if (action == null || itemIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/admin/gallery?error=Invalid request");
            return;
        }
        
        try {
            int itemId = Integer.parseInt(itemIdStr);
            GalleryItem item = GalleryItemDAO.getGalleryItemById(itemId);
            
            if (item == null) {
                response.sendRedirect(request.getContextPath() + "/admin/gallery?error=Item not found");
                return;
            }
            
            // Get the owner of the image
            User owner = UserDAO.getUserById(item.getUserId());
            
            if (owner == null) {
                response.sendRedirect(request.getContextPath() + "/admin/gallery?error=User not found");
                return;
            }
            
            if (action.equals("delete")) {
                // Check if the current user has permission to delete this image
                if (AuthService.canDeleteImage(currentUser, owner)) {
                    if (GalleryItemDAO.deleteGalleryItem(itemId)) {
                        response.sendRedirect(request.getContextPath() + "/admin/gallery?message=Image deleted successfully");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/gallery?error=Failed to delete image");
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/gallery?error=You don't have permission to delete this image");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/gallery?error=Invalid action");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/gallery?error=Invalid item ID");
        }
    }
} 