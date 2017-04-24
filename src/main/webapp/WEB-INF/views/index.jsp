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
        <div class="col-xs-12">
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
                <c:forEach var="user" items="${users}">
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