<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>

<title>Add new place</title>
<body>
	<h3>Add new place</h3>
	<br>
	<form:form action="createPlace" method="post">
		<table>
			<tr>
				<td>Place name</td>
				<td><input type="text" name="name" placeholder="place name"
					required></td>
			</tr>

			<tr>
				<td><br> Place type:</td>
				<td><select name="placeTypeName">
						<c:forEach items="${ placeTypes }" var="placeTypeName">
							<option value="${placeTypeName}">${ placeTypeName }</option>
						</c:forEach>
				</select></td>
			</tr>
			<br>

			<tr>
				<td>Street:</td>
				<td><input type="text" name="street" placeholder="Street"
					required></td>
			</tr>
			<tr>
				<td>Street number:</td>
				<td><input type="number" min="1" name="streetNumber"
					placeholder="Street number" required></td>

			</tr>
			<tr>
				<td>City :</td>
				<td><input type="text" name="city" placeholder="City" required>
				</td>
			</tr>
			<tr>
				<td>Country :</td>
				<td><input type="text" name="country" placeholder="Country"
					required></td>
			</tr>

			<tr>
				<td>Photos</td>
				<td>
					<!-- <form method="POST" action="uploadFile"
						enctype="multipart/form-data">
						File to upload: <input type="file" name="file"><br />
						Name: <input type="text" name="name"><br /> <br /> <input
							type="submit" value="Upload"> Press here to upload the
						file!
					</form> -->
				<td></td>
				<tr>
				           <td>Select a file to upload</td>
				           <td><input type="file" name="files" /></td>
				       </tr>
				        <tr>
				           <td>Select a file to upload</td>
				           <td><input type="file" name="files" /></td>
				       </tr>
				        <tr>
				           <td>Select a file to upload</td>
				           <td><input type="file" name="files" /></td>
				       </tr>
			</tr>


		</table>
		<br>
		<button type="submit" value="create">Add new place</button>
	</form:form>
</body>
</html>