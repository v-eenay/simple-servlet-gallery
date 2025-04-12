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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserListServlet", urlPatterns = {"/admin/list-users"})
public class UserListServlet extends HttpServlet {

    private static final int USERS_PER_PAGE = 10; // Number of users to display per page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Get pagination parameters
        int page = 1; // Default to first page
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            }
        } catch (NumberFormatException e) {
            // If page parameter is invalid, default to page 1
            page = 1;
        }

        // Get total count of users for pagination
        int totalUsers;
        List<User> allUsers;

        if (AuthService.hasSuperAdminAccess(currentUser)) {
            // Super admin can see all users
            allUsers = UserDAO.getAllUsers();
        } else {
            // Regular admin can only see non-admin users
            allUsers = UserDAO.getNonAdminUsers();
        }

        totalUsers = allUsers.size();

        // Calculate pagination values
        int totalPages = (int) Math.ceil((double) totalUsers / USERS_PER_PAGE);
        if (page > totalPages && totalPages > 0) {
            page = totalPages; // Adjust if requested page is beyond total pages
        }

        // Get paginated subset of users
        List<User> paginatedUsers = new ArrayList<>();
        int startIndex = (page - 1) * USERS_PER_PAGE;
        int endIndex = Math.min(startIndex + USERS_PER_PAGE, allUsers.size());

        if (startIndex < allUsers.size()) {
            paginatedUsers = allUsers.subList(startIndex, endIndex);
        }

        // Set attributes for the JSP
        request.setAttribute("users", paginatedUsers);
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalUsers", totalUsers);

        // Forward to list-users JSP
        request.getRequestDispatcher("/WEB-INF/view/admin/list-users.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // This can be used for actions like filtering, etc.
        doGet(request, response);
    }
}