<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    String username = user.getFullName();
%>
<%
    String imgmsg = (String) request.getAttribute("imgmsg");
    if (imgmsg!=null){
%>
    <div><%=imgmsg%></div>
<%
    }
%>
<h1>Image Gallery</h1>
<h2>Hello <%=username%>, welcome to the image gallery</h2>
<a href="${pageContext.request.contextPath}/addimage">Add new image</a><br>
<a href="${pageContext.request.contextPath}/viewgallery">View images</a><br>
<a href="${pageContext.request.contextPath}/logout">Logout</a>
</body>
</html>