<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>
<c:set var="maxImages" value="2"/>
<title>Add new place</title>
<body>
	<h3>Add new place</h3>
	<br>
	<form:form action="createPlace" method="post" enctype="multipart/form-data">
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
				<td>Price :</td>
				<td><input type="number" name="price" placeholder="Price"
					step="any" min="0" required></td>
			</tr>
			<tr>
				<td>Photos</td>
				<td>
					<!-- <div class="container">
						<input type="file" name="file" id="file" style="display: none">

						Drag and Drop container
						<div class="upload-area" id="uploadfile"
							style="width: 70%; height: 200px; border: 2px solid lightgray; border-radius: 3px; margin: 0 auto; margin-top: 0px; text-align: center; overflow: auto;">
							<h2
								style="text-align: center; font-weight: normal; font-family: sans-serif; line-height: 50px; color: darkslategray;">
								Drag and Drop images here<br />Or<br />Click to select
							</h2>
						</div>
					</div> --> 
					<%-- <form method="POST" action="uploadFile"
						enctype="multipart/form-data">
						File to upload: <input type="file" name="file"><br />
						Name: <input type="text" name="name"><br /> <br /> <input
							type="submit" value="Upload"> Press here to upload the
						file!
					</form>  --%>
				<!-- <td></td> -->
				<!-- 
				<tr>
				<td>Select a file to upload</td>
				<td><input type="file" name="file" /></td>
			</tr>
			<tr>
				<td>Select a file to upload</td>
				<td><input type="file" name="file" /></td>
			</tr>
			<tr>
				<td>Select a file to upload</td>
				<td><input type="file" name="file" /></td>
			</tr> -->
			<c:forEach begin="0" end="${ maxImages }">
        		<input type="hidden" name="MAX_FILE_SIZE" value="5242880" /> 
        	    <input class="selected-images" type="file" name="files" accept="image/*" />
			</c:forEach> 
		</table>
		<br>
		<td>
		<button id="upload" type="submit" value="create">Add new place</button>
	</form:form>
</body>
</html>