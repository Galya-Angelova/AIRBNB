<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>
<title>My places</title>
</head>
<body>

	<br>
	<c:forEach items="${ userPlaces }" var="place">
	<form:form action="myPlaces" method="get">
		<table>
			<tr>
				<td>Place name</td>
				<td><c:out value="${place.name}"/></td>
			</tr>

			<tr>
				<td> Place type:</td>
				<td><c:out value="${place.placeTypeName}"/></td>
			</tr>

			<tr>
				<td>Street:</td>
				<td><c:out value="${place.street}"/></td>
			</tr>
			<tr>
				<td>Street number:</td>
				<td><c:out value="${place.streetNumber}"/></td>

			</tr>
			<tr>
				<td>City :</td>
				<td><c:out value="${place.city}"/>
				</td>
			</tr>
			<tr>
				<td>Country :</td>
				<td><c:out value="${place.country}"/></td>
			</tr>
			<tr>
			<td>Price: </td>
			<td> <c:out value="${place.price}"/> BGN</td>
			</tr>
		</table>
		
	</form:form>
	<br>-------------------------------------------------------------------
	</c:forEach>
</body>
</html>