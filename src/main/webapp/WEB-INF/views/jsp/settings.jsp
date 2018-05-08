<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>Settings</title>
<jsp:include page="navigation.jsp"></jsp:include>

<title>Settings</title>
</head>
<body>
	<h1>Settings</h1>
	<form action="updateSettings" method="post">
		<div class="panel-body">
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_email"> Email </label>
				<div class="col-sm-9">

					<input id="user_email" name="email" size="30" type="email"
						value="${user.email}" />
				</div>
			</div>

			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_first_name">
					First Name </label>
				<div class="col-sm-9">

					<input id="user_first_name" name="firstName" size="30" type="text"
						value="${user.firstName}" />
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_last_name">
					Last Name </label>
				<div class="col-sm-9">

					<input id="user_last_name" name="lastName" size="30" type="text"
						value="${user.lastName}" />
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_old_password">
					Old password </label>
				<div class="col-sm-9">
				<input onchange="this.setCustomValidity(validity.valueMissing ? 'Please entrer your old password' : '');"
				 id="password" type="password" required name="oldPassword" size="30" value="" placeholder="input old password">
					<!-- <input type="password" name="oldPassword" size="30" value="" placeholder="input old password" required> -->
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_new_password">
					New password </label>
				<div class="col-sm-9">
					<input type="password" name="newPassword" size="30">
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_new_password_confirm">
					Confirm new password </label>
				<div class="col-sm-9">
					<input type="password" name="newPasswordConfirm" size="30">
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_new_phone_number">
					Phone number </label>
				<div class="col-sm-9">
					<input type="tel" name="phoneNumber" size="30" title="Phone number"
						value="${user.phoneNumber}" />
				</div>
			</div>

			 <input type="checkbox" name="delete" value="true"> Delete account<br>
			<button type="submit" class="btn btn-primary btn-large">
				Save</button>
			<!-- <br> <input type="submit" value="SaveProfil"> <br> -->
			</div>
	</form>

<script type="text/javascript">
  document.getElementById("password").setCustomValidity("Please entrer your old password");
</script>
</body>
</html>