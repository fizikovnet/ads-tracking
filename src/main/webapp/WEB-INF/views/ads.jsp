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
            <h1>Список объявлений</h1>
            <%--${listAds}--%>
            <c:if test="${not empty listAds}">

                <ul>
                    <c:forEach var="ad" items="${listAds}">
                        <li>
                            <p>Нзвание: ${ad.title}</p>
                            <p>Краткое описание: ${ad.description}</p>
                            <p>Сылка: ${ad.link}</p>
                            <p>ID: ${ad.id}</p>
                            <p>URL ID: ${ad.urlId}</p>
                            <p>Is send: ${ad.send}</p>
                        </li>
                    </c:forEach>
                </ul>

            </c:if>
        </div>
    </div>
</div>
</body>
</html>