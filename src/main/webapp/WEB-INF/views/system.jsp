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
    <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
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
            <h2>Параметры системы</h2>
            <table class="table table-hover table-bordered">
                <tr>
                    <th>
                        Статус системы
                    </th>
                    <th>
                        Общее количество объявлений в базе данных
                    </th>
                    <th>
                        Общее количество зарегистрированных URL
                    </th>
                    <th></th>
                </tr>
                <tr>
                    <td>
                        <c:choose>
                            <c:when test="${sys.status}">
                                <div class="alert alert-success" role="alert">Работает</div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-danger" role="alert">Приостановлена</div>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${sys.adsCount}</td>
                    <td>${sys.urlCount}</td>
                    <td>
                        <c:choose>
                            <c:when test="${sys.status}">
                                <a class="btn btn-info" href="/system/pause">Приостановить работу</a>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-info" href="/system/launch">Возобновить работу</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
            <hr>
            <h2>Таблица зарегистрированных пользователей</h2>
            <table class="table table-hover table-bordered">
                <tr>
                    <th>
                        #
                    </th>
                    <th>
                        Имя
                    </th>
                    <th>
                        Логин
                    </th>
                    <th>
                        Роль
                    </th>
                </tr>
                <c:forEach var="user" items="${sys.users}">
                    <tr>
                        <td>
                                ${user.getId()}
                        </td>
                        <td><a href="/user?id=${user.getId()}">${user.getFullName()}</a></td>
                        <td>${user.getLogin()}</td>
                        <td>${user.getRole()}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
</body>
</html>