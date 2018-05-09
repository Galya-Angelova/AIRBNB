<!DOCTYPE html>
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags"%>
<html lang="en">
<head>
<link rel="shortcut icon" type="image/png" href="img/favicon.ico" />
<title>AIRBNB</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Lato">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<!-- Color Libraries -->
<link rel="stylesheet"
	href="https://www.w3schools.com/lib/w3-colors-metro.css">
<link rel="stylesheet"
	href="https://www.w3schools.com/lib/w3-colors-highway.css">
<link rel="stylesheet"
	href="https://www.w3schools.com/lib/w3-colors-2017.css">
<link rel="stylesheet"
	href="https://www.w3schools.com/lib/w3-colors-food.css">
<link rel="stylesheet"
	href="https://www.w3schools.com/lib/w3-colors-vivid.css">
<link rel="stylesheet" href="css/color-theme.css">
<link rel="stylesheet" href="css/html_slider.css">

<!-- JavaScript -->
<script src="js/utility.js"></script>

</head>
<body>

	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<c:url var="URL" value="index">
					<%-- <c:param name="param" value="${parameter}" />  --%>
				</c:url>
				<a class="navbar-brand" href="<c:out value="${URL}"/>"> AIRBNB</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><c:url var="URL" value="search">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a href="<c:out value="${URL}"/>"><sht:message
							code="header.search" /></a></li>

			</ul>
			<ul class="nav navbar-nav navbar-right">
				 <li><c:url var="URL" value="register">
						<c:param name="param" value="${parameter}" />
					</c:url> <a href="<c:out value="${URL}"/>"><span
						class="glyphicon glyphicon-user"></span> <sht:message
							code="header.sign" /></a></li> 
				<li><c:url var="URL" value="login">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a href="<c:out value="${URL}"/>"><span
						class="glyphicon glyphicon-log-in"></span> <sht:message code="header.login"/></a></li>
					
						<a href="?language=en">
						<img src="img/enFlag.jpg" />
						</a>
						<a href="?language=es">
						<img src="img/esFlag.png" />
						</a>
			</ul>
		</div>
	</nav>
</head>

<body id="page-top" class="index">

	<br>
	<br>