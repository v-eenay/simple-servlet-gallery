package com.example.verysimpleimagegallery.controller.admin;

import com.example.verysimpleimagegallery.dao.GalleryItemDAO;
import com.example.verysimpleimagegallery.dao.UserDAO;
import com.example.verysimpleimagegallery.model.ActivityLog;
import com.example.verysimpleimagegallery.model.User;
import com.example.verysimpleimagegallery.service.ActivityLogService;
import com.example.verysimpleimagegallery.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private static final int ACTIVITY_LOG_LIMIT = 10; // Number of activity logs to display

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Verify admin access
        if (!AuthService.hasAdminAccess(currentUser)) {
            response.sendRedirect(request.getContextPath() + "/?error=You do not have permission to access admin pages");
            return;
        }

        // Get counts for dashboard stats
        int totalUsers = UserDAO.getUserCount();
        int adminCount = UserDAO.getAdminCount();
        int imageCount = GalleryItemDAO.getImageCount();

        // Get recent activity logs
        List<ActivityLog> activityLogs = ActivityLogService.getRecentActivityLogs(ACTIVITY_LOG_LIMIT);

        // Set attributes to be used in the JSP
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("adminCount", adminCount);
        request.setAttribute("imageCount", imageCount);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("activityLogs", activityLogs);

        // Forward to dashboard page
        request.getRequestDispatcher("/WEB-INF/view/admin/admin-dashboard.jsp").forward(request, response);
    }
}