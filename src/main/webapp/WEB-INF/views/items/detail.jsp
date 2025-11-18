<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Item detail</title>
</head>
<body>
<h1>Item detail</h1>

<p><strong>Title:</strong> ${item.title}</p>
<p><strong>Creator:</strong> ${item.creator}</p>
<p><strong>Release year:</strong> ${item.releaseYear}</p>
<p><strong>Genre:</strong> ${item.genre}</p>
<p><strong>Media type:</strong> ${item.mediaType}</p>
<p><strong>Synopsis:</strong><br/>${item.synopsis}</p>

<p>
    <a href="${pageContext.request.contextPath}/home">Back to list</a> |
    <a href="${pageContext.request.contextPath}/home?action=form&id=${item.id}">Edit</a>
</p>

</body>
</html>
