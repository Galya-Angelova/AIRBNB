<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>

<link rel="stylesheet" href="css/gallery.css">

<title>Place</title>
</head>
<body>
	<c:choose>
		<c:when test="${place != null}">
			<form action="editPlace" method="post" enctype="multipart/form-data">
				<div class="panel-body">
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_name"> Name
						</label>
						<div class="col-sm-9">

							<input id="placeName" name="name" size="30" type="text"
								value="${place.name}" /> <input id="id" name="id" type="hidden"
								value="${place.id }" maxlength="150"/>
						</div>
					</div>
					<%-- <div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_busied">
							Busied </label>
						<div class="col-sm-9">

							<input id="busied" name="isBusied" size="30" type="text"
								value="${place.isBusied}" />
						</div>
					</div> --%>

					<%-- <div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_address">
							City </label><br>
							<!-- <label class="text-right col-sm-3" for="place_city"> -->
							City  </label><br>
						<div class="col-sm-9">

							<input id="city" name="city" size="30"
								type="text" value="${place.city}" />
						</div>
					</div> --%>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_city"> City
						</label>
						<div class="col-sm-9">

							<input id="city" name="city" size="30" type="text"
								value="${place.city}" pattern="[A-Za-z]{1,100}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="100"/>
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_country">
							Country </label>
						<div class="col-sm-9">

							<input id="country" name="country" size="30" type="text"
								value="${place.country}" pattern="[A-Za-z]{1,150}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="150"/>
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_street">
							Street </label>
						<div class="col-sm-9">
							<input type="text" name="street" size="30"
								value="${place.street }" pattern="[A-Za-z]{1,150}" title="Not including numbers or special characters (latin letters only)" style="text-transform: capitalize;" required maxlength="150">
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_street_number">
							Street number </label>
						<div class="col-sm-9">
							<input type="number" name="streetNumber" min="1" size="30"
								value="${place.streetNumber }">
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_type_name">
							Place type: </label>
						<div class="col-sm-9">
							<select name="placeTypeName">
								<c:forEach items="${ placeTypes }" var="placeTypeName">
									<option value="${placeTypeName}">${ placeTypeName }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_price">
							Price </label>
						<div class="col-sm-9">
							<input type="number" name="price" size="30"
								value="${place.price}" />
						</div>
					</div>
					<input type="hidden" name="dateOfPosting"
						value="${place.dateOfPosting }" />

					<%-- <c:if test="${place.photosURLs.size() == 0 }">
										<h5>
											<b>No images. </b>
										</h5>
										<br>
									</c:if>
									<c:if test="${place.photosURLs.size() > 0 }">
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
					<jsp:include page="photoGallery.jsp">
						<jsp:param name="place" value="${place}" />
					</jsp:include>
					<%--		<c:forEach begin="0" end="${ 2 }">
        		 <input type="hidden" name="MAX_FILE_SIZE" value="5242880" /> 
        	    <input class="selected-images" type="file" name="files" accept="image/*" />
			</c:forEach> --%>
					<button type="submit" class="btn btn-primary btn-large"> 
					Save
					</button>
				</div>
			</form>

		</c:when>
		<%-- <c:otherwise>
			
		</c:otherwise> --%>
	</c:choose>
	<br>

</body>
</html>