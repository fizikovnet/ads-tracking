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
        <div class="col-xs-12">
            <div style="width: 400px; margin: 0 auto">
                <h1 style="text-align: center">Ads Tracking</h1>
                <h3 style="text-align: center">Вход в Личный кабинет</h3>
                <form action="login" method="post" id="loginForm">
                    <div class="form-group">
                        <label class="control-label">Логин</label>
                        <input type="text" name="login" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Пароль</label>
                        <input type="password" name="password" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">Войти</button>
                    </div>
                </form>
            </div>
            <p class="error_msg">${requestScope.error}</p>
        </div>
    </div>
</div>
</body>
</html>