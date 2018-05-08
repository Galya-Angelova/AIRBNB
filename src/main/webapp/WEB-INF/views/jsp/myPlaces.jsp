<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<jsp:include page="navigation.jsp"></jsp:include>
<title><sht:message code="myPlaces.title"/></title>
</head>
<body>

<div id="contents" class="w3-container menus w3-right"
		style="width: 70%; margin-right: 4%">
		<h1><sht:message code="myPlaces.title"/></h1>
		<c:choose>
			<c:when test="${userPlaces.isEmpty()}">
				<h5>
				"${place.id }"
					<b><sht:message code="myPlaces.noResults"/>. </b>
				</h5>
				<br>
			</c:when>
			<c:otherwise>
				<c:forEach var="place" items="${userPlaces}">
					<div class="w3-container w3-border w3-round-xxlarge w3-white"
						style="margin-top: 2%">
						<div class="w3-row-padding">
							
							<div class="w3-col w3-container w3-margin" style="width: 45%;">

								<div class="w3-container">
									<%-- <a href="place/${place.id}"> <span
										class="w3-large w3-text-highway-blue"> --%><b>${place.name}</b></span><br>
										<br>
									<c:if test="${place.photosURLs.size() == 0 }">
										<h5>
											<b><sht:message code="myPlaces.noImages"/>. </b>
										</h5>
										<br>
									</c:if>
									<c:if test="${place.photosURLs.size() > 0 }">
										<div class="et_pb_gallery_items et_post_gallery"
											data-per_page="3">
											<c:forEach items="${place.photosURLs }" var="url" begin="0"
												end="0">
												<span
													class="et_pb_gallery_item et_pb_grid_item et_pb_bg_layout_light">
													<span class="et_pb_gallery_image landscape"> <a
														href="data:image/jpeg;base64,${url}"
														title="27747975_2074584502761278_1171650822192642176_o">
															<img width=300px src="data:image/jpeg;base64,${url}"
															data-lazy-src="data:image/jpeg;base64,${url}"
															alt="27747975_2074584502761278_1171650822192642176_o"
															class="lazyloaded">
													</a>
												</span>
												</span>
											</c:forEach>
										</div>
									</c:if>


								</div>
							</div>
							<div class="w3-col w3-container w3-margin" style="width: 45%;">

								<div class="w3-container">
									<span class="w3-medium w3-text-theme w3-text-theme">
										<sht:message code="myPlaces.address"/>: <br>
									</span> <span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.city"/>:</b> <span
										class="w3-small w3-text-black ">${place.city}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.country"/>:</b> <span
										class="w3-small w3-text-black ">${place.country}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.street"/>:</b> <span
										class="w3-small w3-text-black ">${place.street}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.streetNumber"/>:</b> <span class="w3-small w3-text-black ">${place.streetNumber}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.placeType"/>:</b> <span class="w3-small w3-text-black ">${place.placeTypeName}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b><sht:message code="myPlaces.priceNight"/>:</b> <span class="w3-small w3-text-black ">${place.price}
											<sht:message code="myPlaces.euro"/></span> </span><br>
											 <span class="w3-medium w3-text-theme "><b><sht:message code="myPlaces.postingDate"/>:</b> <span class="w3-small w3-text-black ">${place.dateOfPosting}
											</span> </span><br>
											<input type="hidden" value = "${place.id }" name = "id"/>
											<c:url var="URL" value="editPlace">
		<c:param name="id" value="${place.id}" /> 
	</c:url>
	<a href="<c:out value="${URL}"/>">Edit place</a>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>

</body>
</html>