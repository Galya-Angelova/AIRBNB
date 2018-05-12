<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.time.LocalDate"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags"%>
<title><sht:message code="login.title" /></title>
<link rel="stylesheet" href="css/loginCss.css">
<jsp:include page="header.jsp"></jsp:include>
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>


<form class="login100-form validate-form" action="login" method="post">
	<div class="login-wrap">
		<div class="login-html">
			<input id="tab-1" type="radio" name="tab" class="sign-in" checked><label for="tab-1" class="tab"><sht:message code="header.login"/></label>
		<input id="tab-2" type="radio" name="tab" class="sign-up"><label for="tab-2" class="tab"></label>
			<div class="login-form">
				<br>
				<br>
				<br>
				<br>
				<div class="sign-in-htm">
					<div class="group">
						<label for="email" class="label"><sht:message code="login.email"/></label> <input id="email"
							name="email" type="email" class="input" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required maxlength="45">
					</div>
					<div class="group">
						<label for="password" class="label"><sht:message code="login.password"/></label> <input
							id="password" name="password" type="password" class="input"
							data-type="password" required>
					</div>
					<div class="group">
						<input type="submit" class="button" value="Login">
					</div>
				</div>

			</div>
		</div>
	</div>
</form>

<%-- <table>
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
		<c:param name="param" value="${parameter}" />
	</c:url>
	<a href="<c:out value="${URL}"/>"><sht:message code="login.register"/></a> --%>

<%-- <div class="modal fade" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
    	  <div class="modal-dialog">
				<div class="loginmodal-container">
					<h1><sht:message code="login.title"/></h1>
				  <form action="login" method="post">
				  <sht:message code="login.email"/>
				  <input  id="email"  required name="email" type = "email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required maxlength="45">
			 <input type="email" name="email" required>
			 <sht:message code="login.password"/>
					<input type="password" name="password" placeholder="password">
					<!-- <input type="submit" value="Login"><br> -->
					<input type="submit" name="login" class="login loginmodal-submit" value="Login">
				  </form>
					
				  <div class="login-help">
				  <c:url var="URL" value="register">
				  </c:url>
	<a href="<c:out value="${URL}"/>"><sht:message code="login.register"/></a>
				  </div>
				</div>
			</div>
		  </div>
		   --%>

<!-- <script type="text/javascript">
  document.getElementById("password").setCustomValidity("Please insert your password");
 
</script> -->
</body>
</html>