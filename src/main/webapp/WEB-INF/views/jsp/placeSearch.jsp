<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
	<c:when test="${user != null}">
		<jsp:include page="navigation.jsp"></jsp:include>
	</c:when>
	<c:otherwise>
		<jsp:include page="header.jsp"></jsp:include>
	</c:otherwise>
</c:choose>
<title>Search</title>
</head>
<body>
	<jsp:include page="slideshow.jsp"><jsp:param name="allPhotosURLs" value="${allPhotosURLs}" />
		<jsp:param name="place" value="${place}" />
	</jsp:include>

	<div align="center">
		<h3>Search</h3>
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
					class="w3-small w3-btn w3-block w3-theme-d6  w3-left-align">Ordering</button>
				<div id="orderingSection" class="w3-container w3-show w3-padding ">
					<form:select path="orderedBy" id="orderedBySelector"
						class="w3-select w3-border w3-round-xxlarge" name="option">
						<form:option value="name">Alphabetically</form:option>
						<form:option value="price">By Price</form:option>
						<form:option value="city">By city(alphabetically)</form:option>
						<form:option value="placeType">By place type</form:option>
					</form:select>
					<form:radiobutton path="isAscending" value="true" />
					Ascending<br>
					<form:radiobutton path="isAscending" value="false" />
					Descending<br>
				</div>
			</div>

			<div class="w3-panel">
				<button type="button"
					onclick="openOrCloseSection('placeNameSection')"
					class="w3-small w3-btn w3-block w3-theme-d6  w3-left-align">Place
					Name</button>
				<div id="placeNameSection" class="w3-container w3-show w3-padding ">
					<form:input id="filterName" path="placeName" name="filterName"
						class="w3-container w3-input" type="text" maxlength="100"
						placeholder="Enter place name" />
				</div>
			</div>

			<div class="w3-panel">
				<button type="button"
					onclick="openOrCloseSection('cityNameSection')"
					class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align">City
					Name</button>
				<div id="cityNameSection" class="w3-container w3-show  w3-padding ">
					<form:select path="city" id="citySelector"
						class="w3-select w3-border w3-round-xxlarge" name="option">
						<form:option value="All">All</form:option>
						<form:options items="${cities}" />
					</form:select>
				</div>
			</div>

			<div class="w3-panel">
				<button type="button" onclick="openOrCloseSection('priceSection')"
					class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align">Price</button>
				<div id="priceSection" class="w3-container w3-show  w3-padding ">
					<div class="slidecontainer">
						<span>Min: <span id="minPriceForNight">${editedFilter.minPriceForNight}</span></span>
						<form:input path="minPriceForNight" type="range"
							min="${filter.minPriceForNight}" max="${filter.maxPriceForNight}"
							value="${editedFilter.minPriceForNight}" class="slider"
							id="minPriceSlider"
							oninput="getSliderValue('minPriceSlider','minPriceForNight')" />
					</div>
					<div class="slidecontainer">
						<span>Max: <span id="maxPriceForNight">${editedFilter.maxPriceForNight}</span></span>
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
					class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align">Place
					Type</button>
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
		<h1>Filtered products</h1>
		<c:choose>
			<c:when test="${allPlaces.isEmpty()}">
				<h5>
					<b>No results found. </b>
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
											<b>No images. </b>
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
										Address: <br>
									</span> <span class="w3-medium w3-text-theme "><b>City:</b> <span
										class="w3-small w3-text-black ">${place.city}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>Country:</b> <span
										class="w3-small w3-text-black ">${place.country}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>Street:</b> <span
										class="w3-small w3-text-black ">${place.street}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>Street
											number:</b> <span class="w3-small w3-text-black ">${place.streetNumber}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b>Place
											type:</b> <span class="w3-small w3-text-black ">${place.placeTypeName}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b>Price
											for night:</b> <span class="w3-small w3-text-black ">${place.price}
											euro</span> </span><br> <span class="w3-medium w3-text-theme "><b>Date
											of posting:</b> <span class="w3-small w3-text-black ">${place.dateOfPosting}
									</span> </span><br> <br> <input type="hidden" value="${place.id }"
										name="id" /> <a href="reservation/${place.id}"> <span
										class="w3-medium w3-text-highway-blue"><b>Make
												reservation</b></span>
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