<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>
<c:set var="maxImages" value="3"/>
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
				<td></td>
				<td>
					
			<b>Choose thumbnail: </b>
			<input type="hidden" name="MAX_FILE_SIZE" value="5242880" /> 
        	    <input class="selected-images" type="file" name="files" accept="image/*" />
        		<input type="hidden" name="MAX_FILE_SIZE" value="5242880" /> 
        		<b>Choose photos</b>
			<c:forEach begin="0" end="${ maxImages }" varStatus="loop">
			
        	    <input class="selected-images" type="file" name="files" accept="image/*" />
        	    
			</c:forEach> 
		</table>
		<br>
		<td>
		<button id="upload" type="submit" value="create">Add new place</button>
	</form:form>
</body>
</html>