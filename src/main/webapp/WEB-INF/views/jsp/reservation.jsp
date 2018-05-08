<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>

<jsp:include page="navigation.jsp"></jsp:include>
<title><sht:message code="reservation.title"/></title>
</head>
<body>
	<form:form action="reservation" method="post">
		<table>
			<tr>
				<td><sht:message code="reservation.from"/>: </td>
				<td><input type="date" name="startDate" min=<%= LocalDate.now() %>
					max=<%= LocalDate.now().plusYears(1) %> required></td>
			</tr>
			<tr>
				<td><sht:message code="reservation.to"/>: </td>
				<td><input type="date" name="endDate" min=<%= LocalDate.now() %>
					max=<%= LocalDate.now().plusYears(1) %> required></td>
			</tr>
			<tr>
			<td> <input id="id" name="id" type="hidden" value="${place.id }" />
			</td>
			<td>
			<sht:message code="reservation.forPlace"/>:<c:out value="${place.id }"/>
			</td>
			</tr>
			<c:if test="${wrongDates}">Invalid dates! The end date must be after the start date!</c:if> 
			<c:if test="${sameUser}">You are the owner of that place!</c:if> 
		</table>
		<br> <input type="submit" value="Make reservation"> <br>
	</form:form>
</body>
</html>