<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <h2>Управление URL</h2>
            <c:choose>
                <c:when test="${model.url != null}">
                    <table class="table table-hover table-bordered">
                        <tr>
                            <th>URL</th>
                            <th></th>
                            <th>Валидность</th>
                            <th>Активность</th>
                            <th>Количество объявлений</th>
                            <th></th>
                        </tr>
                        <c:if test="${model.url != null}">
                            <tr>
                                <td>
                                    <a href="${model.url.getUrl()}">${model.url.getUrl()}</a>
                                </td>
                                <td>
                                    <a href="/ads" class="btn btn-default">Просмотр объявлений</a>
                                </td>
                                <td>
                                    <a class="btn btn-primary" href="/validate-url?urlId=${model.url.getId()}">Проверить URL</a>
                                    <hr>
                                    <c:if test="${not empty sessionScope.validateUrl}" >
                                        <c:choose>
                                            <c:when test="${sessionScope.validateUrl}">
                                                <p class="label label-success">URL Корретен</p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="label label-danger">URL Не Корретен!</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </td>
                                <td>
                                        ${model.url.isActive() ? "<div class='alert alert-success' role='alert'>Активно</div>" : "<div class='alert alert-warning' role='alert'>Не активно</div>"}
                                </td>
                                <td>${model.adsCount}</td>
                                <td>
                                    <a class="btn btn-info" href="/redact-url?id=${model.url.getId()}">Редактировать URL</a>
                                    <hr>
                                    <a class="btn btn-warning" href="/clean-ads?id=${model.url.getId()}">Удалить объявления</a>
                                    <hr>
                                    <a class="btn btn-danger" href="/delete-url?id=${model.url.getId()}">Удалить URL</a>
                                </td>
                            </tr>
                        </c:if>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>URL не зарегистрирован</p>
                    <p><a href="/add-url" class="btn btn-success">Добавить URL</a></p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>