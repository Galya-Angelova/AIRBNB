<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<title><sht:message code="login.title"/></title>
<jsp:include page="header.jsp"></jsp:include>

<h1><sht:message code="login.title"/></h1>
<form action="login" method="post">

	<table>
		<tr>
			<td><sht:message code="login.email"/></td>
			<td><input  id="email"  required name="email" type = "email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required maxlength="45">
			<!-- <input type="email" name="email" required></td> -->
		</tr>
		<tr>
			<td><sht:message code="login.password"/></td>
			<td><input  id="password"  required name="password" type = "password"></td>
			<!-- <input type="password" name="password" required></td> -->
		</tr>
	</table>
	<input type="submit" value="Login"><br> <sht:message code="login.noAccount"/>?
	<c:url var="URL" value="register">
	<%-- 	<c:param name="param" value="${parameter}" /> --%>
	</c:url>
	<a href="<c:out value="${URL}"/>"><sht:message code="login.register"/></a>
</form>

<!-- <script type="text/javascript">
  document.getElementById("password").setCustomValidity("Please insert your password");
 
</script> -->
</body>
</html>