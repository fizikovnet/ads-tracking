<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <div class="col-xs-4 wrapper">
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
            <h2>Данные пользователя ${model.user.getFullName()}</h2>
            <table class="table table-hover table-bordered">
                <tr>
                    <td>#</td>
                    <td>Имя</td>
                    <td>Логин</td>
                    <td>Роль</td>
                    <td></td>
                </tr>
                <tr>
                    <td>${model.user.getId()}</td>
                    <td>${model.user.getFullName()}</td>
                    <td>${model.user.getLogin()}</td>
                    <td>${model.user.getRole()}</td>
                    <td><a class="btn btn-info" href="/change-password">Изменить пароль</a></td>
                </tr>
            </table>
        </div>
    </div>
</div>
</body>
</html>