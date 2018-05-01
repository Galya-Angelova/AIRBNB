<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add new place</title>
</head>
<body>
	<h3>Add new place</h3>
	<br>
	<form action="createPlace" method="post">
		<table>
			<tr>
				<td>ProjectName <input type="text" name="placeName"
					placeholder="place name" required>
				</td>
			</tr>

			<tr>
				<td><br> Place type: <select name="placeType">
						<c:forEach items="${ placeTypes }" var="p">
							<option value="${ p.id }">${ p.type.getValue() }</option>
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