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
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-4">
            <h2>Данные пользователя ${model.user.getFullName()}</h2>
            <table class="table table-hover table-bordered">
                <tr>
                    <td>#</td>
                    <td>Имя</td>
                    <td>Логин</td>
                    <td>Роль</td>
                </tr>
                <tr>
                    <td>${model.user.getId()}</td>
                    <td>${model.user.getFullName()}</td>
                    <td>${model.user.getLogin()}</td>
                    <td>${model.user.getRole()}</td>
                </tr>
            </table>
        </div>
        <div class="col-xs-12">
            <h2>Список зарегистрированных URL</h2>
            <table class="table table-hover table-bordered">
                <tr>
                    <th>URL</th>
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
                                ${model.url.isActive() ? "Активно" : "Не активно"}
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
        </div>
    </div>
</div>
</body>
</html>