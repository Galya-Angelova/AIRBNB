<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ page import="java.time.LocalDate"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib prefix="sht" uri="http://www.springframework.org/tags"%>
	
	<title><sht:message code="register.title" /></title>

<link rel="stylesheet" href="css/loginCss.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<jsp:include page="header.jsp"></jsp:include>

<body>

	<div >
	<form class="login100-form validate-form" action="register" method="post">
		<div class="login-html"  style="height: 150%;">
		<label style="color: white;"  for="tab-2" class="tab">Sign Up</label>
			<div class="login-form">
				<div>
					<div class="group">
						<label for="user" class="label"><sht:message
						code="register.email" /></label> <input id="email" name="email"
							type="email" class="input" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required maxlength="45">
					</div>
					<div class="group">
						<label for="pass" class="label"><sht:message
						code="register.password" /></label> <input id="pass" name = "password"
							type="password" class="input" data-type="password" required>
					</div>
					<div class="group">
						<label for="pass" class="label"><sht:message
						code="register.passConf" /></label> <input
							id="pass2" type="password"  name = "confirmPassword"class="input" data-type="password" required>
					</div>
					<div class="group">
						<label for="user" class="label"><sht:message
						code="register.fName" /></label> <input id="firstName" name="firstName"
							type="text" class="input" required>
					</div>
					<div class="group">
						<label for="user" class="label"><sht:message
						code="register.lName" /></label> <input id="lastName" name="lastName"
							type="text" class="input" required>
					</div>
					<div class="group" style="color: #FFF8DC">
						<label for="user" class="label" ><sht:message
						code="register.gender" /></label> <input id="gender"
							type="radio" name="isMale" class="input" value="true"><sht:message code="register.male" /><br>
							<input id="gender"
							type="radio" name="isMale" class="input" value="false"><sht:message code="register.female" />
					</div>
					<div class="group">
						<label for="user" class="label"><sht:message
						code="register.birthday" /></label> <input id="bday" name="bday" min="1900-01-02" max="<%=LocalDate.now()%>"
							type="date" class="input" required>
					</div>
					<div class="group">
						<label for="user" class="label"><sht:message
						code="register.phone" /></label> <input id="phone" name="phone"
							type="tel" class="input" pattern="08\d{8}"
					title="Starting with 08 and 10 symbols long" required datatype="tel">
					</div>
					<div class="group">
						<input type="submit" class="button" value="Sign Up">
					</div>
					
				</div>
			</div>
		</div>
		</form>
	</div>
	
</body>
</html>