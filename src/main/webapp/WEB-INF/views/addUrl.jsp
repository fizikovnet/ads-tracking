<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login Page</title>
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 wrapper">
            <nav class="navbar navbar-inverse navbar-fixed-top">
                <div class="container">
                    <ul class="nav navbar-nav">
                        <a class="navbar-brand" href="/index">Ads Tracking</a>
                        <li><a href="/user?id=${sessionScope.user.id}">Личные данные</a></li>
                        <li><a href="/url">Управление URL</a></li>
                        <c:if test="${sessionScope.user.role == 'ADMIN'}">
                            <li><a href="/system">Параметры системы</a></li>
                        </c:if>
                        <li><a href="/logout">Выйти</a></li>
                    </ul>
                </div>
            </nav>
            <h2>Добавление URL</h2>
            <form:form method="POST" action="add-url" commandName="url" >
                <div class="form-group" style="width: 400px">
                    <form:label path="url">URL:</form:label>
                    <form:input path="url" cssClass="form-control"/>
                </div>
                <div class="form-group">
                    <form:checkbox path="active" label="Активно"/>
                </div>
                <div class="form-group">
                    <input type="submit" value="Сохранить" class="btn btn-primary">
                </div>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>