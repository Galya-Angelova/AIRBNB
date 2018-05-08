<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.LocalDate" %>
<title>Register</title>
<jsp:include page="header.jsp"></jsp:include>
<%Exception e = (Exception) request.getAttribute("exception");  %>
<h1>Register</h1>
<form method="post" action="register" onsubmit="return validateForm();">
	<table>
		<tr>
			<td>Email</td>
			<td><input name="email" type="email" required></td>
		</tr>
		<tr>
			<td>Password</td>
			<td><input name="password" type="password" onchange="return validatePassowrd();" title="Your password should be at least 8 characters, with at least one capital letter, at least one lowercase letter, at leat one digit and special character: '!-_@#$%^&+='"></td>
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
				max="<%=LocalDate.now() %>" required></td>
		</tr>
		<tr>
			<td>Phone number</td>
			<td><input type="tel" name="phone" required></td>
	</table>
	<input
		onchange="this.setCustomValidity(validity.valueMissing ? 'Please indicate that you accept the Terms and Conditions' : '');"
		id="field_terms" type="checkbox" required name="terms"> I
	accept the Terms and Conditions <br> <input type="submit"
		value="Register"> <br> Already have an account?
	<c:url var="URL" value="index">
		<%-- <c:param name="param" value="${parameter}" /> --%>
	</c:url>
	<a href="<c:out value="${URL}"/>">Login here</a>

</form>


<%-- <span id="error"><%=e.getMessage() %></span> --%>
<script type="text/javascript">
	document.getElementById("field_terms").setCustomValidity(
			"Please indicate that you accept the Terms and Conditions");
	function validatePassword(){
		var password = document.getElementsByName('password')[0].value;
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!-_@#$%^&+=])(?=\\S+$).{8,}";
		if (!password.matches(pattern)) {
			document.getElementById("error").innerHTML = "Your password should be at least 8 characters, with at least one capital letter, at least one lowercase letter, at leat one digit and special character: '!-_@#$%^&+='";
			alert("Your password should be at least 8 characters, with at least one capital letter, at least one lowercase letter, at leat one digit and special character: '!-_@#$%^&+='")
			return false;
		}
	}
	function validateForm() {
		
		var email = document.getElementsByName('email')[0].value;
		if(!email.contains('@')){
			document.getElementById("error").innerHTML = "Invalid email";
			alert('Invalid email ')
			return false;
		}
		
		
		
		var firstName = document.getElementsByName('firstName')[0].value;
		if (firstName.trim().length <= 3) {
			document.getElementById("error").innerHTML = "at least 3 letters required in name";
			alert('Name should be at least 3 letters: ')
			return false;
		}
		var lastName = document.getElementsByName('lastName')[0].value;
		if (lastName.trim().length <= 3) {
			document.getElementById("error").innerHTML = "at least 3 letters required in name";
			alert('Name should be at least 3 letters: ')
			return false;
		}
		
		var password2 = document.getElementsByName('confirmPassword')[0].value;
		if(!password.equals(password2)){
			document.getElementById("error").innerHTML = "Passwords mismatch";
			alert("Passwords mismatch")
			return false;
		}
		
		var phoneRegex = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/;
		var phoneNumber = document.getElementsByName('phone')[0].value;
		function telephoneCheck(phoneNumber) {
			  var isphone = phoneRegex.test(str);
			  alert(isphone);
			}
	}
</script>
</body>
</html>