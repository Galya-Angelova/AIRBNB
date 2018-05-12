<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<jsp:include page="navigation.jsp"></jsp:include>
<c:set var="maxImages" value="3"/>
<title><sht:message code="addNewPlace.title"/></title>
<body>
	<h3><sht:message code="addNewPlace.title"/></h3>
	<br>
	<form:form action="createPlace" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td><sht:message code="addNewPlace.placeName"/></td>
				<td><input type="text" name="name" placeholder="place name"
					required maxlength="150"></td>
			</tr>

			<tr>
				<td><br> <sht:message code="addNewPlace.placeType"/>:</td>
				<td><select name="placeTypeName">
						<c:forEach items="${ placeTypes }" var="placeTypeName">
							<option value="${placeTypeName}">${ placeTypeName }</option>
						</c:forEach>
				</select></td>
			</tr>

			<tr>
				<td><sht:message code="addNewPlace.street"/>:</td>
				<td><input type="text" name="street" placeholder="Street"
					title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="150"></td>
			</tr>
			<tr>
				<td><sht:message code="addNewPlace.streetNumber"/>:</td>
				<td><input type="number" min="1" name="streetNumber"
					placeholder="Street number" required></td>

			</tr>
			<tr>
				<td><sht:message code="addNewPlace.city"/> :</td>
				<td><input type="text" name="city" placeholder="City" pattern="[A-Za-z]{1,100}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="100">
				</td>
			</tr>
			<tr>
				<td><sht:message code="addNewPlace.country"/> :</td>
				<td><input type="text" name="country" placeholder="Country"
					pattern="[A-Za-z]{1,150}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="150"></td>
			</tr>
			<tr>
				<td><sht:message code="addNewPlace.price"/> :</td>
				<td><input type="number" name="price" placeholder="Price"
					step="any" min="0" required></td>
			</tr>
			<tr>
				<td></td>
				<td>
					
			<b><sht:message code="addNewPlace.thumbnail"/>: </b>
			<input type="hidden" name="MAX_FILE_SIZE" value="5242880" /> 
        	    <input class="selected-images" type="file" name="files" accept="image/*" />
        		<input type="hidden" name="MAX_FILE_SIZE" value="5242880" /> 
        		<b><sht:message code="addNewPlace.choosePhotos"/></b>
			<c:forEach begin="0" end="${ maxImages }" varStatus="loop">
			
        	    <input class="selected-images" type="file" name="files" accept="image/*" />
        	    
			</c:forEach> 
		</table>
		<br>
		<td>
		<button id="upload" type="submit" value="create"><sht:message code="addNewPlace.title"/></button>
	</form:form>
</body>
</html>