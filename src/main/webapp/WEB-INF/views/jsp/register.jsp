<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<title><sht:message code="register.title"/></title>
<jsp:include page="header.jsp"></jsp:include>
<%Exception e = (Exception) request.getAttribute("exception");  %>
<h1><sht:message code="register.title"/></h1>
<form method="post" action="register" onsubmit="return validateForm();">
	<table>
		<tr>
			<td><sht:message code="register.email"/></td>
			<td><input name="email" type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required maxlength="45"></td>
		</tr>
		<tr>
			<td><sht:message code="register.password"/></td>
			<td><input name="password" id ="pass" type="password" onchange="return validatePassowrd();" title="Your password should be at least 8 characters, with at least one capital letter, at least one lowercase letter and at leat one digit"></td>
		</tr>
		<tr>
			<td><sht:message code="register.passConf"/></td>
			<td><input name="confirmPassword" id ="cpass" type="password" required></td>
		</tr>
		<tr>
			<td><sht:message code="register.gender"/></td>
			<td><input type="radio" name="isMale" value="true"> <sht:message code="register.male"/><br>
				<input type="radio" name="isMale" value="false"> <sht:message code="register.female"/><br>
			</td>
		</tr>
		<tr>
			<td><sht:message code="register.fName"/></td>
			<td><input name="firstName" type="text" pattern="[A-Za-z]{1,100}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="100"></td>
		</tr>
		<tr>
			<td><sht:message code="register.lName"/></td>
			<td><input name="lastName" type="text" pattern="[A-Za-z]{1,100}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="100"></td>
		</tr>
		<tr>
			<td><sht:message code="register.birthday"/></td>
			<td><input type="date" name="bday" min="1900-01-02"
				max="<%=LocalDate.now() %>" required></td>
		</tr>
		<tr>
			<td><sht:message code="register.phone"/></td>
			<td><input type="tel" name="phone"  pattern="08\d{8}" title="Starting with 08 and 10 symbols long"required maxlength="20"></td>
	</table>
	<input
		onchange="this.setCustomValidity(validity.valueMissing ? 'Please indicate that you accept the Terms and Conditions' : '');"
		id="field_terms" type="checkbox" required name="terms"> <sht:message code="register.terms"/><br> <input type="submit"
		value="Register"> <br> <sht:message code="register.haveAccount"/>?
	<c:url var="URL" value="index">
		<%-- <c:param name="param" value="${parameter}" /> --%>
	</c:url>
	<a href="<c:out value="${URL}"/>"><sht:message code="register.logHere"/></a>

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
	/* function validateForm() {
		
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
	} */
</script>

</body>
</html>