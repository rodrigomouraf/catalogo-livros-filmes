<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Item Form</title>
</head>
<body>
<h1><c:if test="${empty item.id}">New</c:if><c:if test="${not empty item.id}">Edit</c:if> Item</h1>

<form action="${pageContext.request.contextPath}/home" method="post">
    <input type="hidden" name="action" value="save"/>
    <c:if test="${not empty item.id}">
        <input type="hidden" name="id" value="${item.id}"/>
    </c:if>

    <p>
        <label>Title:
            <input type="text" name="title" value="${item.title}" required/>
        </label>
    </p>
    <p>
        <label>Creator:
            <input type="text" name="creator" value="${item.creator}"/>
        </label>
    </p>
    <p>
        <label>Release year:
            <input type="number" name="releaseYear" value="${item.releaseYear}"/>
        </label>
    </p>
    <p>
        <label>Genre:
            <input type="text" name="genre" value="${item.genre}"/>
        </label>
    </p>
    <p>
        <label>Synopsis:<br/>
            <textarea name="synopsis" rows="4" cols="40">${item.synopsis}</textarea>
        </label>
    </p>
    <p>
        <label>Media type:
            <select name="mediaType" required>
                <option value="">-- select --</option>
                <option value="Book"  ${item.mediaType == 'Book'  ? 'selected' : ''}>Book</option>
                <option value="Movie" ${item.mediaType == 'Movie' ? 'selected' : ''}>Movie</option>
                <option value="Series"${item.mediaType == 'Series'? 'selected' : ''}>Series</option>
            </select>
        </label>
    </p>

    <p>
        <button type="submit">Save</button>
        <a href="${pageContext.request.contextPath}/home">Cancel</a>
    </p>
</form>

</body>
</html>
