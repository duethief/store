<%--
  Created by IntelliJ IDEA.
  User: InSeok
  Date: 2014-04-01
  Time: 오전 9:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <h3>c:forEach example code</h3>
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
                    <td>${book.name}</td>
                    <c:set var="status" value="일반"/>
                    <c:choose>
                        <c:when test="${book.status eq 'CanRent'}">
                            <c:set var="status" value="일반"/>
                        </c:when>
                        <c:when test="${book.status eq 'RentNow'}">
                            <c:set var="status" value="대여중"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="status" value="분실중"/>
                        </c:otherwise>
                    </c:choose>
                    <td>${status}</td>
                    <td>${book.comment}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>