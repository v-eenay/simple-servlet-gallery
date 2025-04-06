package com.example.verysimpleimagegallery.controller.admin;

import com.example.verysimpleimagegallery.dao.UserDAO;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UserActionServlet", urlPatterns = {"/admin/user-action"})
public class UserActionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Redirect GET requests to the user list page
        response.sendRedirect(request.getContextPath() + "/admin/list-users");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // Get action and user ID from parameters
        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");
        
        if (action == null || userIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/admin/list-users?error=Invalid request");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            User targetUser = UserDAO.getUserById(userId);
            
            if (targetUser == null) {
                response.sendRedirect(request.getContextPath() + "/admin/list-users?error=User not found");
                return;
            }
            
            // Don't allow users to modify themselves
            if (currentUser.getId() == userId) {
                response.sendRedirect(request.getContextPath() + "/admin/list-users?error=You cannot modify your own account");
                return;
            }
            
            switch (action) {
                case "delete":
                    if (AuthService.canDeleteUser(currentUser, targetUser)) {
                        if (UserDAO.deleteUser(userId)) {
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?message=User deleted successfully");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?error=Failed to delete user");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/list-users?error=You don't have permission to delete this user");
                    }
                    break;
                    
                case "makeAdmin":
                    // Only super admin can make others admin
                    if (AuthService.hasSuperAdminAccess(currentUser)) {
                        if (UserDAO.updateUserRole(userId, 0)) { // 0 = admin
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?message=User promoted to admin");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?error=Failed to update user role");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/list-users?error=You don't have permission to make users admin");
                    }
                    break;
                    
                case "revokeAdmin":
                    // Only super admin can revoke admin status
                    if (AuthService.hasSuperAdminAccess(currentUser)) {
                        if (UserDAO.updateUserRole(userId, 1)) { // 1 = regular user
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?message=Admin privileges revoked");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?error=Failed to update user role");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/list-users?error=You don't have permission to revoke admin status");
                    }
                    break;
                    
                case "makeSuperAdmin":
                    // Only super admin can make others super admin
                    if (AuthService.hasSuperAdminAccess(currentUser)) {
                        if (UserDAO.updateUserRole(userId, 2)) { // 2 = super admin
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?message=User promoted to super admin");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/admin/list-users?error=Failed to update user role");
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/admin/list-users?error=You don't have permission to make users super admin");
                    }
                    break;
                    
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/list-users?error=Invalid action");
                    break;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/list-users?error=Invalid user ID");
        }
    }
} 