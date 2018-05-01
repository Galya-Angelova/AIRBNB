<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>Settings</title>
<jsp:include page="navigation.jsp"></jsp:include>
<title>Settings</title>
</head>
<body>
	<h1>Settings</h1>
	<form action="/updateSettings" method="post">
		<div class="panel-body">

			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_first_name">
					First Name </label>
				<div class="col-sm-9">

					<input id="user_first_name" name="firstName" size="30" type="text"
						value="${user.getFirsName}" />
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_last_name">
					Last Name </label>
				<div class="col-sm-9">

					<input id="user_last_name" name="lastName" size="30" type="text"
						value="${user.getLastName}" />
				</div>
			</div>
			<div class="row row-condensed space-4">
				<label class="text-right col-sm-3" for="user_old_password">
					Old password </label>
				<div class="col-sm-9">
					<input type="password" name="oldPassword" size="30" value="">
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
					<input type="tel" name="phoneNumber" size="30">
				</div>
			</div>
			<!-- <div class="row row-condensed space-4">
				<div class="col-sm-9">
					<input type="submit" name="updateProfile" value="Update profile">
				</div>
			</div> -->
			<button type="submit" class="btn btn-primary btn-large">
				Save</button>
	</form>


</body>
</html>