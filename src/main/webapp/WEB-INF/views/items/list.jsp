<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Media Catalog</title>
</head>
<body>
<h1>Media Catalog</h1>

<form action="${pageContext.request.contextPath}/home" method="get">
    <input type="hidden" name="action" value="search"/>
    <input type="text" name="query" value="${fn:escapeXml(query)}" placeholder="Search by title or creator"/>
    <button type="submit">Search</button>
</form>

<p><a href="${pageContext.request.contextPath}/home?action=form">New item</a></p>

<table border="1" cellpadding="5">
    <thead>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Creator</th>
        <th>Year</th>
        <th>Genre</th>
        <th>Type</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${items}">
        <tr>
            <td>${item.id}</td>
            <td>${item.title}</td>
            <td>${item.creator}</td>
            <td>${item.releaseYear}</td>
            <td>${item.genre}</td>
            <td>${item.mediaType}</td>
            <td>
                <a href="${pageContext.request.contextPath}/home?action=detail&id=${item.id}">Detail</a> |
                <a href="${pageContext.request.contextPath}/home?action=form&id=${item.id}">Edit</a> |
                <form action="${pageContext.request.contextPath}/home" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete"/>
                    <input type="hidden" name="id" value="${item.id}"/>
                    <button type="submit" onclick="return confirm('Delete this item?');">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
