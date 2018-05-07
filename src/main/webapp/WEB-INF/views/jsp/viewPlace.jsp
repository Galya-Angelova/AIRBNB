<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>
<title>Places</title>
</head>
<body>

	<br>
	<form:form action="viewPlace" method="get">
		<table>
			<tr>
				<td>Place name</td>
				<td><c:out value="${place.name}" /></td>
			</tr>

			<tr>
				<td><br> Place type:</td>
				<td><c:out value="${place.placeType.name}" /></td>
			</tr>

			<tr>
				<td>Street:</td>
				<td><c:out value="${place.address.street}" /></td>
			</tr>
			<tr>
				<td>Street number:</td>
				<td><c:out value="${place.address.streetNumber}" /></td>

			</tr>
			<tr>
				<td>City :</td>
				<td><c:out value="${place.address.city.name}" /></td>
			</tr>
			<tr>
				<td>Country :</td>
				<td><c:out value="${place.address.country.name}" /></td>
			</tr>
			<tr>
				<td>Price:</td>
				<td><c:out value="${place.price}" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>