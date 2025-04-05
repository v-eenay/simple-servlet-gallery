<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<%
    // Check if user is not logged in
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect(request.getContextPath()+"/login");
        return;
    }
%>
<h1>Image Gallery</h1>
<h2>Hello <%= username %>, welcome to the image gallery</h2>
<a href="${pageContext.request.contextPath}/addimage">Add new image</a><br>
<a href="${pageContext.request.contextPath}/viewgallery">View images</a><br>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
</body>
</html>