<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="en">
<head>
<!-- <style>
.dropdown:hover .dropdown-content {
	display: block;
}

.navbar a:hover, .dropdown:hover .dropbtn {
	background-color: black;
}

.dropdown-content {
	display: none;
	position: absolute;
	background-color: #f9f9f9;
	min-width: 160px;
	box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
	z-index: 1;
}

.dropdown-content a {
	float: none;
	color: black;
	padding: 12px 16px;
	text-decoration: none;
	display: block;
	text-align: left;
}

.dropdown-content a:hover {
	background-color: #ddd;
}

.dropdown:hover .dropdown-content {
	display: block;
}
</style> -->


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<!-- Color Libraries -->
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-colors-metro.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-colors-highway.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-colors-2017.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-colors-food.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-colors-vivid.css">
<link rel="stylesheet" href="css/color-theme.css">
<link rel="stylesheet" href="css/html_slider.css">

<!-- JavaScript -->
<script src="js/utility.js"></script>


<link rel="shortcut icon" type="image/png" href="img/favicon.ico" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<base href="http://localhost:8080/airbnb/">
</head>
<body>

	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<c:url var="URL" value="search">
					<%-- <c:param name="param" value="${parameter}" />--%>
				</c:url> 
				<a class="navbar-brand" href="<c:out value="${URL}"/>"> AIRBNB</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><c:url var="URL" value="updateSettings">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a href="<c:out value="${URL}"/>">Settings</a></li>

			</ul>
			<ul class="nav navbar-nav">

				<%-- 		<li class="active"><c:url var="URL" value="createPlace">
								<c:param name="param" value="${parameter}" />
							</c:url> <a href="<c:out value="${URL}"/>">Add new place</a></li> --%>

				<%-- <c:if test="${theBooleanVariable ne true}">It's false!</c:if> --%>
				<%-- 	 <c:set var="isHost"  scope="session"  value="${user.isHost}" />--%>
				<c:choose>
					<c:when test="${!user.isMyPlacesEmpty()}">

						<li class="active"><c:url var="URL" value="createPlace">
								<%-- <c:param name="param" value="${parameter}" /> --%>
							</c:url> <a href="<c:out value="${URL}"/>">Add new place</a></li>
						<ul class="nav navbar-nav">
						<li class="active"><c:url var="URL" value="myPlaces">
					<%-- 		<c:param name="param" value="${parameter}" /> --%>
							</c:url> <a href="<c:out value="${URL}"/>">My places</a></li>

						</ul>	
					</c:when>
					<c:otherwise>
						<li class="active"><c:url var="URL" value="createPlace">
								<%-- <c:param name="param" value="${parameter}" /> --%>
							</c:url> <a href="<c:out value="${URL}"/>">Become a host</a></li>
					</c:otherwise>
				</c:choose>
			</ul>

			
			
			<ul class="nav navbar-nav">
				<li class="active"><c:url var="URL" value="visitedPlaces">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a href="<c:out value="${URL}"/>">Visited places</a></li>

			</ul>
			<%-- <ul class="nav navbar-nav">
				<li class="active"><c:url var="URL" value="allPlaces">
						<c:param name="param" value="${parameter}" />
					</c:url> <a href="<c:out value="${URL}"/>">All places</a></li>

			</ul>  --%>
			
			<ul class="nav navbar-nav">
				<li class="active"><c:url var="URL" value="search">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a href="<c:out value="${URL}"/>">Search</a></li>

			</ul>
			
			<ul class="nav navbar-nav navbar-right">

				<li><c:url var="URL" value="logout">
						<%-- <c:param name="param" value="${parameter}" /> --%>
					</c:url> <a class="navbar-brand" href="<c:out value="${URL}"/>"><span
						class="glyphicon glyphicon-log-out"></span> Log out</a></li>
			</ul>
		</div>
	</nav>