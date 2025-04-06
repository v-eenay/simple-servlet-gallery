<%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/6/2025
  Time: 11:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Management - Image Gallery</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <div class="container">
        <%
            String message = request.getParameter("message");
            if (message == null) {
                message = (String) request.getAttribute("message");
            }
            if (message != null) {
        %>
            <div class="message success"><%=message%></div>
        <%
            }
            
            String error = request.getParameter("error");
            if (error == null) {
                error = (String) request.getAttribute("error");
            }
            if (error != null) {
        %>
            <div class="message error"><%=error%></div>
        <%
            }
            
            User currentUser = (User) session.getAttribute("user");
            List<User> users = (List<User>) request.getAttribute("users");
            if (users == null) {
                users = new ArrayList<>();
            }
        %>
        
        <h1>User Management</h1>
        <h2>Manage user accounts</h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="button">Back to Dashboard</a>
            <% if (currentUser.isSuperAdmin()) { %>
                <a href="${pageContext.request.contextPath}/admin/add-admin" class="button">Add Admin</a>
            <% } %>
        </div>
        
        <div class="user-filters">
            <div class="search-box">
                <input type="text" id="userSearch" placeholder="Search users..." />
            </div>
            <div class="filter-options">
                <label class="filter-label">
                    <input type="checkbox" id="filterAdmins" />
                    <span>Show Admins Only</span>
                </label>
            </div>
        </div>

        <div class="users-table-container">
            <table class="users-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (User user : users) { 
                        // Skip current user
                        if (user.getId() == currentUser.getId()) continue;
                        
                        String roleText = "Regular User";
                        if (user.isAdmin()) roleText = "Admin";
                        if (user.isSuperAdmin()) roleText = "Super Admin";
                    %>
                    <tr data-role="<%=user.getRole()%>">
                        <td><%=user.getId()%></td>
                        <td><%=user.getFullName()%></td>
                        <td><%=user.getEmail()%></td>
                        <td><%=roleText%></td>
                        <td class="actions">
                            <% if (currentUser.isSuperAdmin() || (!user.isAdmin() && !user.isSuperAdmin())) { %>
                                <% if (currentUser.isSuperAdmin()) { %>
                                    <% if (user.isAdmin()) { %>
                                        <form action="${pageContext.request.contextPath}/admin/user-action" method="post" style="display:inline">
                                            <input type="hidden" name="action" value="revokeAdmin">
                                            <input type="hidden" name="userId" value="<%=user.getId()%>">
                                            <button type="submit" class="action-link">Revoke Admin</button>
                                        </form>
                                    <% } else if (user.isSuperAdmin()) { %>
                                        <form action="${pageContext.request.contextPath}/admin/user-action" method="post" style="display:inline">
                                            <input type="hidden" name="action" value="revokeAdmin">
                                            <input type="hidden" name="userId" value="<%=user.getId()%>">
                                            <button type="submit" class="action-link">Revoke Super Admin</button>
                                        </form>
                                    <% } else { %>
                                        <form action="${pageContext.request.contextPath}/admin/user-action" method="post" style="display:inline">
                                            <input type="hidden" name="action" value="makeAdmin">
                                            <input type="hidden" name="userId" value="<%=user.getId()%>">
                                            <button type="submit" class="action-link">Make Admin</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/admin/user-action" method="post" style="display:inline">
                                            <input type="hidden" name="action" value="makeSuperAdmin">
                                            <input type="hidden" name="userId" value="<%=user.getId()%>">
                                            <button type="submit" class="action-link">Make Super Admin</button>
                                        </form>
                                    <% } %>
                                <% } %>
                                
                                <form action="${pageContext.request.contextPath}/admin/user-action" method="post" style="display:inline">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="userId" value="<%=user.getId()%>">
                                    <button type="submit" class="action-link delete" onclick="return confirm('Are you sure you want to delete this user? This action cannot be undone.')">Delete</button>
                                </form>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                    
                    <% if (users.isEmpty()) { %>
                    <tr>
                        <td colspan="5" style="text-align: center;">No users found</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <div class="pagination">
            <a href="#" class="pagination-link active">1</a>
            <a href="#" class="pagination-link">2</a>
            <a href="#" class="pagination-link">3</a>
            <span class="pagination-separator">...</span>
            <a href="#" class="pagination-link">10</a>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
    <script>
        // Simple client-side search functionality
        document.getElementById('userSearch').addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const rows = document.querySelectorAll('.users-table tbody tr');
            
            rows.forEach(row => {
                const name = row.children[1].textContent.toLowerCase();
                const email = row.children[2].textContent.toLowerCase();
                
                if (name.includes(searchTerm) || email.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
        
        // Filter for admins
        document.getElementById('filterAdmins').addEventListener('change', function() {
            const rows = document.querySelectorAll('.users-table tbody tr');
            
            if (this.checked) {
                rows.forEach(row => {
                    const role = parseInt(row.getAttribute('data-role'));
                    if (role === 0 || role === 2) { // Admin or Super Admin
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                });
            } else {
                rows.forEach(row => {
                    row.style.display = '';
                });
            }
        });
    </script>
</body>
</html>
