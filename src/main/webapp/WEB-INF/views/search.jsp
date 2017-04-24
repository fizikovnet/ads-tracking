<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>
<div class="container-fluid" style="margin-bottom: 20px">
	<div class="row">
		<div class="col-xs-12">
			<h1>Advanced search</h1>
			<p><a href="show-items">&larr; Return to show items</a></p>
			<form:form method="POST" action="advanced-search" commandName="searchParameters" cssClass="advSearch" >
				<div class="form-group col-xs-4">
					<form:label path="itemId">Item UID:</form:label>
					<form:input path="itemId" cssClass="form-control"/>
					<form:errors path="itemId" cssClass="errors"/>
				</div>
				<div class="form-group col-xs-4">
					<form:label path="title">Title:</form:label>
					<form:input path="title" cssClass="form-control"/>
					<form:errors path="title" cssClass="errors"/>
				</div>
				<div class="form-group col-xs-4">
					<form:label path="description">Description:</form:label>
					<form:input path="description" cssClass="form-control"/>
					<form:errors path="description" cssClass="errors"/>
				</div>
				<div class="form-group col-xs-4">
					<form:label path="minPrice">Min. price:</form:label>
					<form:input path="minPrice" cssClass="form-control"/>
					<form:errors path="minPrice" cssClass="errors"/>
				</div>
				<div class="form-group col-xs-4">
					<form:label path="maxPrice">Max. price:</form:label>
					<form:input path="maxPrice" cssClass="form-control"/>
					<form:errors path="maxPrice" cssClass="errors"/>
				</div>
				<div class="form-group col-xs-4">
					<form:radiobutton path="buyItNow" value="true" label="Show only buy it now items"/>
				</div>
			<div class="form-group col-xs-4">
				<hr>
			</div>
				<div class="form-group col-xs-4">
					<form:label path="startDate">Start date:</form:label>
					<form:input path="startDate" cssClass="form-control"/>
					<form:errors path="startDate" cssClass="errors"/>
				</div>
				<div class="form-group col-xs-4">
					<form:label path="expireDate">Expire date:</form:label>
					<form:input path="expireDate" cssClass="form-control"/>
					<form:errors path="expireDate" cssClass="errors"/>
					<p>Format Date: DD/MM/YY</p>
				</div>
				<div class="form-group col-xs-4">
					<hr>
				</div>
				<div class="form-group col-xs-4">
					<form:label path="bidderCount">Bidder count:</form:label>
					<form:input path="bidderCount" cssClass="form-control"/>
					<form:errors path="bidderCount" cssClass="errors"/>
					</div>
				<div class="form-group">
					<div class="col-xs-1">
						<input type="submit" value="Search" class="btn btn-primary">
					</div>
					<div class="col-xs-1">
						<a href="advanced-search?clear=1" class="btn btn-primary">Clear Search</a>
					</div>
				</div>

			</form:form>
		</div>
	</div>
</div>
</body>
</html>
