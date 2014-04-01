<%--
  Created by IntelliJ IDEA.
  User: InSeok
  Date: 2014-04-01
  Time: 오전 9:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title></title>
</head>
<body>
    <table>
        <thead>
            <tr>
                <th>Title</th>
                <th>status</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="book" items="${books}">
                <tr>
                    <td>${book.title}</td>
                    <c:set var="status" value="일반" />
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
