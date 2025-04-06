<%@ page import="com.example.verysimpleimagegallery.model.User" %><%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/5/2025
  Time: 9:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Item</title>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
        String username = user.getFullName();
    %>
    <h1>Welcome <%=username%>, please add now item</h1>
    <form action="${pageContext.request.contextPath}/addimage" method="POST" enctype="multipart/form-data">
        <fieldset>
            <legend>Add Image</legend>
            <label>Title: <input type="text" name="title"></label>
            <label>File: <input type="file" name="image"></label>
            <input type="submit" value="Submit">
        </fieldset>
    </form>
    <%
        String error = request.getParameter("error");
        if(error!=null && error.equals("true")){
    %>
        <div>Error uploading image</div>
    <%
        }
    %>
</body>
</html>
