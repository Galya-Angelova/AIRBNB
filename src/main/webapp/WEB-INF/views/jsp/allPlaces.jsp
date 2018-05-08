<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<jsp:include page="navigation.jsp"></jsp:include>
<title><sht:message code="allPlace.title"/></title>
</head>
<body>
	<c:forEach items="${ allPlaces }" var="place">
		<form:form action="allPlaces" method="get">
			<table>
				<tr>
					<td><sht:message code="addNewPlace.placeName"/></td>
					<td><c:out value="${place.name}" /></td>
				</tr>

				<tr>
					<td><sht:message code="addNewPlace.placeType"/>:</td>
					<td><c:out value="${place.placeTypeName}" /></td>
				</tr>

				<tr>
					<td><sht:message code="addNewPlace.street"/>:</td>
					<td><c:out value="${place.street}" /></td>
				</tr>
				<tr>
					<td><sht:message code="addNewPlace.streetNumber"/>:</td>
					<td><c:out value="${place.streetNumber}" /></td>

				</tr>
				<tr>
					<td><sht:message code="addNewPlace.city"/> :</td>
					<td><c:out value="${place.city}" /></td>
				</tr>
				<tr>
					<td><sht:message code="addNewPlace.country"/> :</td>
					<td><c:out value="${place.country}" /></td>
				</tr>
				<tr>
					<td><sht:message code="addNewPlace.price"/>:</td>
					<td><c:out value="${place.price}" /> BGN</td>
				</tr>
				<tr>
					<td><sht:message code="allPlace.photos"/>:</td>
					<td><c:if test="${place.photosURLs.size() == 0 }">
							<h5>
								<b><sht:message code="myPlaces.noImages"/>. </b>
							</h5>
							<br>
						</c:if></td>
					<c:if test="${place.photosURLs.size() > 0 }">
						<div class="et_pb_gallery_items et_post_gallery" data-per_page="3">
							<c:forEach items="${place.photosURLs }" var="url">
								<span
									class="et_pb_gallery_item et_pb_grid_item et_pb_bg_layout_light">
									<span class="et_pb_gallery_image landscape"> <a
										href="data:image/jpeg;base64,${url}"
										title="27747975_2074584502761278_1171650822192642176_o"> <img
											width=300px src="data:image/jpeg;base64,${url}"
											data-lazy-src="data:image/jpeg;base64,${url}"
											alt="27747975_2074584502761278_1171650822192642176_o"
											class="lazyloaded">
									</a>
								</span>
								</span>
							</c:forEach>
						</div>
					</c:if>
			</table>
			<c:url var="URL" value="reservation">
				<c:param name="param" value="${parameter}" />
			</c:url>
			<a href="<c:out value="${URL}"/>"><sht:message code="details.makeReservation"/></a>
		</form:form>
		<br>---------------------------------------------------
	</c:forEach>
</body>
</html>