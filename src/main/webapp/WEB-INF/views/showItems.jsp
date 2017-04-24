<%@page import="marketplace.Entity.Bid"%>
<%@page import="marketplace.DAO.OracleDAO.DAOFactory"%>
<%@page import="marketplace.DAO.BidDAO"%>
<%@ page import="marketplace.Entity.User" %>
<%@ page import="marketplace.Entity.Item" %>
<%@ page session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Show Items</title>
      <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
      <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
      <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
      <script src="<c:url value="/resources/js/showItems.js" />"></script>
  </head>
  <body>
  <div class="container-fluid">
    <div class="row">
      <div class="col-xs-4 col-xs-offset-8" id="logInfo">
          <%
            if (request.getSession().getAttribute("user") == null) {
                %>
          <div>You are logged in as: <span>Guest</span></div>
          <div><a href="login">login</a></div>
          <%
            } else {
                User user = (User)request.getSession().getAttribute("user");
                %>
          <div>You are logged in as: <span><%=user.getFullName()%></span></div>
          <div><a href="logout">logout</a></div>
          <%
            }
          %>

      </div>
      <div class="col-xs-12">
        <h1>Show Items</h1>
        <p>
          <div class="form-group">
            <a href="advanced-search">Advanced search</a>
          </div>
         </p>
          <p>
              <a href="show-items">Show All Items</a> |
              <a href="show-my-items">Show My Items</a> |
              <a href="sell">Sell</a>
          </p>
        <table class="table table-bordered" id="tableItems">
        <thead>
          <tr>
            <th>UID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Seller</th>
            <th>Start price</th>
            <th>Bid inc.</th>
            <th>Best offer</th>
            <th>Bidder</th>
            <th>Stop date</th>
            <th>Bidding</th>
          </tr>
          </thead>
          <tbody>
          </tbody>
        </table>
      </div>
    </div>
	</div>
  </body>
</html>