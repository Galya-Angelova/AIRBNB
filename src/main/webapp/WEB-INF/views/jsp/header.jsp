<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<link rel="shortcut icon" type="image/png" href="img/favicon.ico"/>
<title>AIRBNB</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
					</c:url> <a href="<c:out value="${URL}"/>">Search</a></li>

			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><c:url var="URL" value="register">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a href="<c:out value="${URL}"/>"><span
						class="glyphicon glyphicon-user"></span> Sign Up</a></li>
				<li><c:url var="URL" value="login">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a href="<c:out value="${URL}"/>"><span
						class="glyphicon glyphicon-log-in"></span> Login</a></li>
			</ul>
		</div>
	</nav>
</head>

<body id="page-top" class="index">

	<!-- Navigation -->
	<!-- 	<nav id="mainNav"
		class="navbar navbar-default navbar-custom navbar-fixed-top">
		<div class="container">
			Brand and toggle get grouped for better mobile display
			<div class="navbar-header page-scroll">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> Menu <i
						class="fa fa-bars"></i>
				</button>
				<a class="navbar-brand page-scroll" href="#page-top">AIRBNB</a>
			</div>

			Collect the nav links, forms, and other content for toggling
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li class="hidden"><a href="#page-top"></a></li>
					<li><a class="page-scroll" href="./index.jsp">Login/Register</a>
					</li>
					<li><a class="page-scroll" href="#team-pointer">Team</a></li>
					<li><a class="page-scroll" href="./contacts">Contact</a></li>
				</ul>
			</div>
		</div>
	</nav> -->
	<br>
	<br>