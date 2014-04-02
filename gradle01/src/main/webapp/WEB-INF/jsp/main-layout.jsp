<%--
  Created by IntelliJ IDEA.
  User: InSeok
  Date: 2014-04-02
  Time: 오전 9:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title><tiles:insertAttribute name="title"/></title>
</head>
<body>
    <tiles:insertAttribute name="content"/>
</body>
</html>
