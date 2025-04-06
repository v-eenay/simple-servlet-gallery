<%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/6/2025
  Time: 11:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="java.util.ArrayList" %>
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
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
            <div class="message success"><%=message%></div>
        <%
            }
            
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="message error"><%=error%></div>
        <%
            }
        %>
        
        <h1>User Management</h1>
        <h2>Manage user accounts</h2>

        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="button">Back to Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/add-admin" class="button">Add Admin</a>
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
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Sample data - This would typically be populated from a backend request -->
                    <tr>
                        <td>1</td>
                        <td>John Doe</td>
                        <td>john@example.com</td>
                        <td>User</td>
                        <td>Active</td>
                        <td class="actions">
                            <a href="#" class="action-link">Edit</a>
                            <a href="#" class="action-link delete">Delete</a>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Jane Smith</td>
                        <td>jane@example.com</td>
                        <td>Admin</td>
                        <td>Active</td>
                        <td class="actions">
                            <a href="#" class="action-link">Edit</a>
                            <a href="#" class="action-link">Revoke Admin</a>
                        </td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Robert Johnson</td>
                        <td>robert@example.com</td>
                        <td>User</td>
                        <td>Inactive</td>
                        <td class="actions">
                            <a href="#" class="action-link">Edit</a>
                            <a href="#" class="action-link">Activate</a>
                            <a href="#" class="action-link delete">Delete</a>
                        </td>
                    </tr>
                    <tr>
                        <td>4</td>
                        <td>Sarah Williams</td>
                        <td>sarah@example.com</td>
                        <td>User</td>
                        <td>Active</td>
                        <td class="actions">
                            <a href="#" class="action-link">Edit</a>
                            <a href="#" class="action-link">Make Admin</a>
                            <a href="#" class="action-link delete">Delete</a>
                        </td>
                    </tr>
                    <tr>
                        <td>5</td>
                        <td>Michael Brown</td>
                        <td>michael@example.com</td>
                        <td>Super Admin</td>
                        <td>Active</td>
                        <td class="actions">
                            <a href="#" class="action-link">Edit</a>
                        </td>
                    </tr>
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
                    const role = row.children[3].textContent;
                    if (role.includes('Admin')) {
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
        
        // Confirm delete action
        document.querySelectorAll('.action-link.delete').forEach(link => {
            link.addEventListener('click', function(e) {
                if (!confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
                    e.preventDefault();
                }
            });
        });
    </script>
</body>
</html>
