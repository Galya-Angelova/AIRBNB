<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<c:choose>
	<c:when test="${user != null}">
		<jsp:include page="navigation.jsp"></jsp:include>
	</c:when>
	<c:otherwise>
		<jsp:include page="header.jsp"></jsp:include>
	</c:otherwise>
</c:choose>
<spring:url value="/webapp/static/css/gallery.css" var="galleryCss" />

<link rel="stylesheet" href="css/gallery.css">
<title><sht:message code="details.title"/></title>

<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
$(document).ready(function(){
    $("button").click(function(){
        $("p").toggle();
    });
});
</script>
</head>
<body>
	<div class="w3-col w3-container w3-margin" style="width: 45%;">


		<div class="w3-container">
			<input type="hidden" value="${place.id }" name="id" />
			<c:url var="URL" value="placeDetails">
				<c:param name="id" value="${place.id}" />
			</c:url>
			<a href="<c:out value="${URL}"/>"> <span
				class="w3-large w3-text-highway-blue"><b>${place.name}</b></span><br>
				<br>
			</a> <a href="<c:out value="${URL}"/>"></a>

			<%-- <c:if test="${place.photosURLs.size() > 0 }">
										<div class="et_pb_gallery_items et_post_gallery"
											data-per_page="3">
											<c:forEach items="${place.photosURLs }" var="url" begin="0"
												end="3">
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
									</c:if> --%>


		</div>
	</div>
	<div class="w3-col w3-container w3-margin" style="width: 45%;">

		<div class="w3-container">
			<span class="w3-medium w3-text-theme w3-text-theme"> <sht:message code="myPlaces.address"/>:
				<br>
			</span> <span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.city"/>:</b> <span
				class="w3-small w3-text-black ">${place.city}</span> </span><br> <span
				class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.country"/>:</b> <span
				class="w3-small w3-text-black ">${place.country}</span> </span><br> <span
				class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.street"/>:</b> <span
				class="w3-small w3-text-black ">${place.street}</span> </span><br> <span
				class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.streetNumber"/>:</b> <span
				class="w3-small w3-text-black ">${place.streetNumber}</span> </span><br>
			<span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.placeType"/>:</b> <span
				class="w3-small w3-text-black ">${place.placeTypeName}</span> </span><br>
			<span class="w3-medium w3-text-theme "><b><sht:message code="myPlaces.priceNight"/>:</b>
				<span class="w3-small w3-text-black ">${place.price} euro</span> </span><br>
			<span class="w3-medium w3-text-theme "><b>D<sht:message code="myPlaces.postingDate"/>:</b>
				<span class="w3-small w3-text-black ">${place.dateOfPosting}
			</span> </span><br>
			
		<c:choose>
			<c:when test="${avgRating != 0}">
			<span class="w3-medium w3-text-theme "><b><sht:message code="details.rating"/>:</b>
				<span class="w3-small w3-text-black ">${avgRating}
			</span></span><br>		
			</c:when>
			<c:otherwise>
					<span class="w3-medium w3-text-theme "><b><sht:message code="details.rating"/>:</b>
				<span class="w3-small w3-text-black "><sht:message code="details.noRating"/>
			</span></span><br>		
			</c:otherwise>
		</c:choose>	
			 <br> <input type="hidden" value="${place.id }"
				name="id" /> <a href="reservation/${place.id}"> <span
				class="w3-medium w3-text-highway-blue"><b><sht:message code="details.makeReservation"/></b></span>
			</a>
		</div>
	</div>
	
	<div class="w3-col w3-container w3-margin" style="width: 45%;">
		<div class="w3-container">
		
		
		
		
		
		<div id="review"
		class="w3-container w3-white w3-round-xxlarge menus"
		style="margin-left: 2%;">
		<form:form modelAttribute="newReview">

			<div class="w3-panel">
				<input class="w3-margin" type="submit" value="Add review" />
			</div>

			<div class="w3-panel">
			<span class="w3-medium w3-text-theme "><b><sht:message code="details.title"/>:</b></span>
				<div id="placeNameSection" class="w3-container w3-show w3-padding ">
					<form:input type="hidden" path="id"/>
					<form:input type="hidden" path="placeId" value="${place.id }"/>
					<form:input type="hidden" path="userId" value="${sessionScope.user.id }"/>
					<form:input id="title" path="title" name="title"
						class="w3-container w3-input" type="text" maxlength="100"
						placeholder="Enter Title here" />
				</div>
			</div>
			<div class="w3-panel">
				<span class="w3-medium w3-text-theme "><b><sht:message code="details.content"/>:</b></span>
				<div id="ContentSection" class="w3-container w3-show w3-padding ">
					<form:textarea id="content" path="text" name="content"
						class="w3-container w3-input" type="text" maxlength="1000"
						placeholder="Enter Content here" rows="5" cols="44" />
				</div>
			</div>
		</form:form>
	</div>
		<span class="w3-medium w3-text-theme w3-text-theme"> <sht:message code="details.reviews"/>:
				<br></span> 
				<button> <sht:message code="details.showHide"/></button><br>
				<c:choose>
			<c:when test="${reviews.isEmpty()}">
				<h5>
					<p><sht:message code="details.noReviews"/>. </p>
				</h5>
				<br>
			</c:when>
			<c:otherwise>
			<c:forEach var="entry" items="${reviews}">
			<span class="w3-small w3-text-black "><p> <sht:message code="details.reviewMade"/>:</p>
				<span class="w3-medium w3-text-theme "><p>${entry.value.firstName} ${entry.value.lastName}</p>
			</span></span>
			<span class="w3-small w3-text-black "><p> <sht:message code="details.title"/>:</p>
				<span class="w3-medium w3-text-theme "><p>${entry.key.title}</p>
			</span></span>
			<span class="w3-small w3-text-black "><p> <sht:message code="details.content"/>:</p>
				<span class="w3-medium w3-text-theme "><p>${entry.key.text}</p>
			</span></span><br>	<br> 
			</c:forEach>
				</c:otherwise>
		</c:choose>
		</div>
	</div>

	<jsp:include page="photoGallery.jsp">
		<jsp:param name="place" value="${place}"/>
	</jsp:include>

</body>
</html>