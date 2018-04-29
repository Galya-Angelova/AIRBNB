<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
			<td><input name="confirm password" type="password" required></td>
		</tr>
		<tr>
			<td>Gender</td>
			<td><select name="gender" style="width: 152px" required>
					<option value="true">Male</option>
					<option value="false">Female</option>
			</select></td>
		</tr>
		<tr>
			<td>First name</td>
			<td><input name="first name" type="text" required></td>
		</tr>
		<tr>
			<td>Last name</td>
			<td><input name="last name" type="text" required></td>
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
	Already have an account?<a href="index.jsp"> Login here</a>

</form>
</body>
</html>