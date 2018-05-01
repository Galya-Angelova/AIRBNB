<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>

<title>Add new place</title>
<body>
	<h3>Add new place</h3>
	<br>
	<form action="createPlace" method="post">
		<table>
			<tr>
				<td>Place name <input type="text" name="placeName"
					placeholder="place name" required>
				</td>
			</tr>

			<tr>
				<td><br> Place type: <select name="placeTypes">
						<c:forEach items="${ placeTypes }" var="p">
							<option value="p">${ p }</option>
						</c:forEach>
				</select></td>
			</tr>
			<br>

			<tr>
				<td>Street: <input type="text" name="street"
					placeholder="Street" required>
				</td>
			</tr>
			<tr>
				<td>Street number: <input type="number" name="streetNumber"
					placeholder="Street number" required>
				</td>
			</tr>
			<tr>
				<td>Country : <input type="text" name="country"
					placeholder="Country" required>
				</td>
			</tr>


		</table>
		<br>
		<button type="submit">Add new place</button>
	</form>
</body>
</html>