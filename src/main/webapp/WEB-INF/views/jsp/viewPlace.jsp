<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<jsp:include page="navigation.jsp"></jsp:include>
<title><sht:message code="viewPlace.title"/></title>
</head>
<body>

	<br>
	<form:form action="viewPlace" method="get">
		<table>
			<tr>
				<td><sht:message code="addNewPlace.placeName"/></td>
				<td><c:out value="${place.name}" /></td>
			</tr>

			<tr>
				<td><br> <sht:message code="addNewPlace.placeType"/>:</td>
				<td><c:out value="${place.placeType.name}" /></td>
			</tr>

			<tr>
				<td><sht:message code="addNewPlace.street"/>:</td>
				<td><c:out value="${place.address.street}" /></td>
			</tr>
			<tr>
				<td><sht:message code="addNewPlace.streetNumber"/>:</td>
				<td><c:out value="${place.address.streetNumber}" /></td>

			</tr>
			<tr>
				<td><sht:message code="addNewPlace.city"/> :</td>
				<td><c:out value="${place.address.city.name}" /></td>
			</tr>
			<tr>
				<td><sht:message code="addNewPlace.country"/> :</td>
				<td><c:out value="${place.address.country.name}" /></td>
			</tr>
			<tr>
				<td><sht:message code="addNewPlace.price"/>:</td>
				<td><c:out value="${place.price}" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>