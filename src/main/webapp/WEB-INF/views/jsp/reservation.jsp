<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>
<title>Reservation</title>
</head>
<body>
	<form:form action="reservation" method="post">
		<table>
			<tr>
				<td>From: </td>
				<td><input type="date" name="startDate" min="2018-04-05"
					max="2019-05-10" required></td>
			</tr>
			<tr>
				<td>To: </td>
				<td><input type="date" name="endDate" min="2018-04-05"
					max="2019-05-10" required></td>
			</tr>
			<tr>
			<td><input type="hidden" name="id" value="${place.id }"  required>
			</td>
			<td>
			place id<c:out value="${place.id }"/>
			</td>
			</tr>
		</table>
		<br> <input type="submit" value="Make reservation"> <br>
	</form:form>
</body>
</html>