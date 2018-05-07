<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.LocalDate" %>


<jsp:include page="navigation.jsp"></jsp:include>
<title>Reservation</title>
</head>
<body>
	<form:form action="reservation" method="post">
		<table>
			<tr>
				<td>From: </td>
				<td><input type="date" name="startDate" min=<%= LocalDate.now() %>
					max=<%= LocalDate.now().plusYears(1) %> required></td>
			</tr>
			<tr>
				<td>To: </td>
				<td><input type="date" name="endDate" min=<%= LocalDate.now() %>
					max=<%= LocalDate.now().plusYears(1) %> required></td>
			</tr>
			<tr>
			<td> <input id="id" name="id" type="hidden" value="${place.id }" />
			</td>
			<td>
			place id<c:out value="${place.id }"/>
			</td>
			</tr>
			<c:if test="${wrongDates}">Invalid dates! The end date must be after the start date!</c:if> 
		</table>
		<br> <input type="submit" value="Make reservation"> <br>
	</form:form>
</body>
</html>