<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<title><sht:message code="header.search"/></title>
</head>
<body>
	<jsp:include page="slideshow.jsp"><jsp:param name="allPhotosURLs" value="${allPhotosURLs}" />
		<jsp:param name="place" value="${place}" />
	</jsp:include>

	<div align="center">
		<h3><sht:message code="header.search"/></h3>
	</div>
	<br>
	<div id="filters"
		class="w3-container w3-white w3-round-xxlarge menus w3-left"
		style="margin-left: 2%; width: 20%">
		<form:form modelAttribute="editedFilter">

			<div class="w3-panel">
				<input class="w3-margin" type="submit" value="Filter" />
				<button type="button"
					onclick="openOrCloseSection('orderingSection')"
					class="w3-small w3-btn w3-block w3-theme-d6  w3-left-align"><sht:message code="search.order"/></button>
				<div id="orderingSection" class="w3-container w3-show w3-padding ">
					<form:select path="orderedBy" id="orderedBySelector"
						class="w3-select w3-border w3-round-xxlarge" name="option">
						<form:option value="name"><sht:message code="search.alphabetically"/></form:option>
						<form:option value="price"><sht:message code="search.byPrice"/></form:option>
						<form:option value="city"><sht:message code="search.byCity"/></form:option>
						<form:option value="placeType"><sht:message code="search.byPlaceType"/></form:option>
					</form:select>
					<form:radiobutton path="isAscending" value="true" />
					<sht:message code="search.asc"/><br>
					<form:radiobutton path="isAscending" value="false" />
					<sht:message code="search.desc"/><br>
				</div>
			</div>

			<div class="w3-panel">
				<button type="button"
					onclick="openOrCloseSection('placeNameSection')"
					class="w3-small w3-btn w3-block w3-theme-d6  w3-left-align"><sht:message code="addNewPlace.placeName"/></button>
				<div id="placeNameSection" class="w3-container w3-show w3-padding ">
					<form:input id="filterName" path="placeName" name="filterName"
						class="w3-container w3-input" type="text" maxlength="150"
						placeholder="Enter place name" />
				</div>
			</div>

			<div class="w3-panel">
				<button type="button"
					onclick="openOrCloseSection('cityNameSection')"
					class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align"><sht:message code="addNewPlace.city"/>
					</button>
				<div id="cityNameSection" class="w3-container w3-show  w3-padding ">
					<form:select path="city" id="citySelector"
						class="w3-select w3-border w3-round-xxlarge" name="option">
						<form:option value="All"><sht:message code="search.all"/></form:option>
						<form:options items="${cities}" />
					</form:select>
				</div>
			</div>

			<div class="w3-panel">
				<button type="button" onclick="openOrCloseSection('priceSection')"
					class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align"><sht:message code="addNewPlace.price"/>Price</button>
				<div id="priceSection" class="w3-container w3-show  w3-padding ">
					<div class="slidecontainer">
						<span><sht:message code="search.min"/>: <span id="minPriceForNight">${editedFilter.minPriceForNight}</span></span>
						<form:input path="minPriceForNight" type="range"
							min="${filter.minPriceForNight}" max="${filter.maxPriceForNight}"
							value="${editedFilter.minPriceForNight}" class="slider"
							id="minPriceSlider"
							oninput="getSliderValue('minPriceSlider','minPriceForNight')" />
					</div>
					<div class="slidecontainer">
						<span><sht:message code="search.max"/>: <span id="maxPriceForNight">${editedFilter.maxPriceForNight}</span></span>
						<form:input path="maxPriceForNight" type="range"
							min="${filter.minPriceForNight}" max="${filter.maxPriceForNight}"
							value="${editedFilter.maxPriceForNight}" class="slider"
							id="maxPriceSlider"
							oninput="getSliderValue('maxPriceSlider','maxPriceForNight')" />
					</div>
				</div>
			</div>

			<div class="w3-panel">
				<button type="button"
					onclick="openOrCloseSection('placeTypesSection')"
					class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align"><sht:message code="addNewPlace.placeType"/></button>
				<div id="placeTypesSection"
					class="w3-container w3-show  w3-padding ">
					<form:checkboxes path="placeTypes" items="${placeTypes}"
						delimiter="<br/>" />
				</div>
			</div>
		</form:form>
	</div>

	<!-- Products section -->
	<div id="contents" class="w3-container menus w3-right"
		style="width: 70%; margin-right: 4%">
		<h1><sht:message code="search.filtered"/></h1>
		<c:choose>
			<c:when test="${allPlaces.isEmpty()}">
				<h5>
					<b><sht:message code="myPlaces.noResults"/>. </b>
				</h5>
				<br>
			</c:when>
			<c:otherwise>
				<c:forEach var="place" items="${allPlaces}">
					<div class="w3-container w3-border w3-round-xxlarge w3-white"
						style="margin-top: 2%">
						<div class="w3-row-padding">
							<!-- <div class="w3-col w3-container w3-margin" style="width:25%;">
				<div class="w3-container w3-padding">
	        		<a href="place/${place1.id}">
	        			<img src="getPic?pic=${place1.mainPic}" title="${place1.name}" class = "w3-round" style="width:100%; height:225px;">
	        		</a>
	        	</div>
	        </div>  -->
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
											<sht:message code="myPlaces.euro"/></span> </span><br> <span class="w3-medium w3-text-theme "><b><sht:message code="myPlaces.postingDate"/>:</b> <span class="w3-small w3-text-black ">${place.dateOfPosting}
									</span> </span><br> <br> <input type="hidden" value="${place.id }"
										name="id" /> <a href="reservation/${place.id}"> <span
										class="w3-medium w3-text-highway-blue"><b><sht:message code="details.makeReservation"/></b></span>
									</a>
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