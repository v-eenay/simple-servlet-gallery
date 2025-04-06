<%@ page import="com.example.verysimpleimagegallery.model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.example.verysimpleimagegallery.model.GalleryItem" %><%--
  Created by IntelliJ IDEA.
  User: koira
  Date: 4/5/2025
  Time: 9:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gallery</title>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
    %>
    <h1>Gallery</h1>
    <h2>Hello <%=user.getFullName()%>
    </h2>
    <table>
        <tr>
            <th>S.N.</th>
            <th>Title</th>
            <th>Image</th>
        </tr>
        <%
            ArrayList<GalleryItem> galleryItems = (ArrayList<GalleryItem>) request.getAttribute("galleryItems");
            for(GalleryItem galleryItem: galleryItems){
        %>
            <tr>
                <td><%=galleryItem.getId()%></td>
                <td><%=galleryItem.getTitle()%></td>
                <td><img src="${pageContext.request.contextPath}/imagedisplay?id=<%=galleryItem.getId()%>" alt="Image"></td>
                <td><a href="${pageContext.request.contextPath}/deleteimage?id=<%=galleryItem.getId()%>">Delete this item</a></td>
            </tr>
        <%
            }
        %>
    </table>
</body>
</html>
