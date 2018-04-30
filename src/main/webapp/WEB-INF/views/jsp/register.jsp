<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Register</title>
<jsp:include page="header.jsp"></jsp:include>

<h1>Register</h1>
<form method="post" action="register">
	<table>
		<tr>
			<td>Email</td>
			<td><input name="email" type="email" required></td>
		</tr>
		<tr>
			<td>Password</td>
			<td><input name="password" type="password" required></td>
		</tr>
		<tr>
			<td>Confirm password</td>
			<td><input name="confirmPassword" type="password" required></td>
		</tr>
		<tr>
			<td>Gender</td>
			<td><input type="radio" name="isMale" value="true"> Male<br>
				<input type="radio" name="isMale" value="false"> Female<br>
			</td>
		</tr>
		<tr>
			<td>First name</td>
			<td><input name="firstName" type="text" required></td>
		</tr>
		<tr>
			<td>Last name</td>
			<td><input name="lastName" type="text" required></td>
		</tr>
		<tr>
			<td>Birthday</td>
			<td><input type="date" name="bday" min="1900-01-02"
				max="2018-05-10" required></td>
		</tr>
		<tr>
			<td>Phone number</td>
			<td><input type="tel" name="phone" required></td>
	</table>

	<br> <input type="submit" value="Register"> <br>

	Already have an account?
	<c:url var="URL" value="index">
		<c:param name="param" value="${parameter}" />
	</c:url>
	<a href="<c:out value="${URL}"/>">Login here</a>

</form>
</body>
</html>