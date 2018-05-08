<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<title><sht:message code="navigation.settings"/></title>
<jsp:include page="navigation.jsp"></jsp:include>

<title><sht:message code="navigation.settings"/></title>
</head>
<body>
	<h1><sht:message code="navigation.settings"/></h1>
	<form action="updateSettings" method="post">
		<div class="panel-body">
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_email"> <sht:message code="register.email"/> </label>
				<div class="col-sm-9">

					<input id="user_email" name="email" size="30" type="email"
						value="${user.email}" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required maxlength="45"/>
				</div>
			</div>

			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_first_name">
					<sht:message code="register.fName"/> </label>
				<div class="col-sm-9">

					<input id="user_first_name" name="firstName" size="30" type="text"
						value="${user.firstName}" pattern="[A-Za-z]{1,100}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="100"/>
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_last_name">
					<sht:message code="register.lName"/> </label>
				<div class="col-sm-9">

					<input id="user_last_name" name="lastName" size="30" type="text"
						value="${user.lastName}" pattern="[A-Za-z]{1,100}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="100" />
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_old_password">
					<sht:message code="settings.oldPass"/> </label>
				<div class="col-sm-9">
				<input onchange="this.setCustomValidity(validity.valueMissing ? 'Please entrer your old password' : '');"
				 id="password" type="password" required name="oldPassword" size="30" value="" placeholder="input old password">
					<!-- <input type="password" name="oldPassword" size="30" value="" placeholder="input old password" required> -->
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_new_password">
					<sht:message code="settings.newPass"/> </label>
				<div class="col-sm-9">
					<input type="password" name="newPassword" size="30">
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_new_password_confirm">
					<sht:message code="settings.newPassConf"/> </label>
				<div class="col-sm-9">
					<input type="password" name="newPasswordConfirm" size="30">
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_new_phone_number">
					<sht:message code="register.phone"/> </label>
				<div class="col-sm-9">
					<input type="tel" name="phoneNumber" size="30" title="Phone number"
						value="${user.phoneNumber}" pattern="08\d{8}" title="Starting with 08 and 10 symbols long"required maxlength="20"/>
				</div>
			</div>

			<input type="hidden" 
										name="id" /> <a href="deleteAccount"> <span
										class="w3-medium w3-text-highway-blue"><b><sht:message code="settings.delete"/></b></span>
									</a>
			<button type="submit" class="btn btn-primary btn-large">
				<sht:message code="editplace.save"/></button>
			<!-- <br> <input type="submit" value="SaveProfil"> <br> -->
			</div>
	</form>

<script type="text/javascript">
  document.getElementById("password").setCustomValidity("Please entrer your old password");
</script>
</body>
</html>