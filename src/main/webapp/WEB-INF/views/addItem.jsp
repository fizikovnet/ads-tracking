<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Add Item Form</title>
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/addItem.js" />"></script>
  </head>
  <body>
  <div class="container-fluid">
    <div class="row">
      <div class="col-xs-12">
        <h1>Add Item Form</h1>
    		<form action="sell" method="post" id="addItemForm">
          <div class="form-group col-xs-4">
            <label class="control-label">Title of Item</label>
            <input type="text" name="title" class="form-control required">
          </div>
          <div class="form-group col-xs-4">
            <label>Description</label>
            <textarea name="description" class="form-control"></textarea>
          </div>
          <div class="form-group col-xs-4">
            <label class="control-label">Start Price</label>
            <input type="text" name="startPrice" class="form-control required">
          </div>
          <div class="form-group col-xs-4">
            <label class="control-label">Bid increment</label>
            <input type="text" name="bidIncrement" class="form-control required">
          </div>
          <div class="form-group col-xs-4">
            <label class="control-label">Time left</label>
            <input type="number" name="timeLeft" min="1" max="99999999" class="form-control required">
          </div>
          <div class="checkbox form-group col-xs-4">
            <label>
              <input type="checkbox" name="buyItNow"> Buy It Now
            </label>
          </div>
          <div class="form-group">
            <div class="col-xs-1">
              <button type="submit" class="btn btn-primary">Publish / Save</button>
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