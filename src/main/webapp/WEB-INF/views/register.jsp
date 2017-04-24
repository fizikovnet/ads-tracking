<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Register Page</title>
  <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
  <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>
<body>
<div class="container-fluid">
  <div class="row">
    <div class="col-xs-12">
      <h1>Register Page</h1>
      <form action="register" method="post" id="registerForm">
        <div class="form-group col-xs-4">
          <label class="control-label">Full name</label>
          <input type="text" name="fullName" class="form-control required" required>
        </div>

        <div class="form-group col-xs-4">
          <label>Login</label>
          <input type="text" name="login" class="form-control required" required>
        </div>
        <div class="form-group col-xs-4">
          <label>Password</label>
          <input type="password" name="password" class="form-control required" required>
        </div>
        <div class="form-group col-xs-4">
          <label>Re-enter password</label>
          <input type="password" name="password2" class="form-control">
        </div>
        <div class="form-group">
          <div class="col-xs-1">
            <button type="submit" class="btn btn-primary">Registration</button>
          </div>
          <div class="col-xs-1">
            <button class="btn btn-primary" type="reset">Reset</button>
          </div>
        </div>
      </form>
      <c:if test="${requestScope.error}">
          <p class="error_msg">${requestScope.error}</p>
      </c:if>
    </div>
  </div>
</div>
</body>
</html>