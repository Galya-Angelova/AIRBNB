<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>
<spring:url value="/webapp/static/css/gallery.css" var="galleryCss" />
<title>Details</title>
<%!int count = 1;%>
<%!int count2 = 1;%>
<%!int count3 = 1;%>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {
	font-family: Verdana, sans-serif;
	margin: 0;
}

* {
	box-sizing: border-box;
}

.row>.column {
	padding: 0 8px;
}

.row:after {
	content: "";
	display: table;
	clear: both;
}

.column {
	float: left;
	width: 25%;
}

/* The Modal (background) */
.modal {
	display: none;
	position: fixed;
	z-index: 1;
	padding-top: 100px;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: auto;
	background-color: black;
}

/* Modal Content */
.modal-content {
	position: relative;
	background-color: #fefefe;
	margin: auto;
	padding: 0;
	width: 90%;
	max-width: 1200px;
}

/* The Close Button */
.close {
	color: white;
	position: absolute;
	top: 10px;
	right: 25px;
	font-size: 35px;
	font-weight: bold;
}

.close:hover, .close:focus {
	color: #999;
	text-decoration: none;
	cursor: pointer;
}

.mySlides {
	display: none;
}

.cursor {
	cursor: pointer
}

/* Next & previous buttons */
.prev, .next {
	cursor: pointer;
	position: absolute;
	top: 50%;
	width: auto;
	padding: 16px;
	margin-top: -50px;
	color: white;
	font-weight: bold;
	font-size: 20px;
	transition: 0.6s ease;
	border-radius: 0 3px 3px 0;
	user-select: none;
	-webkit-user-select: none;
}

/* Position the "next button" to the right */
.next {
	right: 0;
	border-radius: 3px 0 0 3px;
}

/* On hover, add a black background color with a little bit see-through */
.prev:hover, .next:hover {
	background-color: rgba(0, 0, 0, 0.8);
}

/* Number text (1/3 etc) */
.numbertext {
	color: #f2f2f2;
	font-size: 12px;
	padding: 8px 12px;
	position: absolute;
	top: 0;
}

img {
	margin-bottom: -4px;
}

.caption-container {
	text-align: center;
	background-color: black;
	padding: 2px 16px;
	color: white;
}

.demo {
	opacity: 0.6;
}

.active, .demo:hover {
	opacity: 1;
}

img.hover-shadow {
	transition: 0.3s
}

.hover-shadow:hover {
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0
		rgba(0, 0, 0, 0.19)
}
</style>
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
			<span class="w3-medium w3-text-theme w3-text-theme"> Address:
				<br>
			</span> <span class="w3-medium w3-text-theme "><b>City:</b> <span
				class="w3-small w3-text-black ">${place.city}</span> </span><br> <span
				class="w3-medium w3-text-theme "><b>Country:</b> <span
				class="w3-small w3-text-black ">${place.country}</span> </span><br> <span
				class="w3-medium w3-text-theme "><b>Street:</b> <span
				class="w3-small w3-text-black ">${place.street}</span> </span><br> <span
				class="w3-medium w3-text-theme "><b>Street number:</b> <span
				class="w3-small w3-text-black ">${place.streetNumber}</span> </span><br>
			<span class="w3-medium w3-text-theme "><b>Place type:</b> <span
				class="w3-small w3-text-black ">${place.placeTypeName}</span> </span><br>
			<span class="w3-medium w3-text-theme "><b>Price for night:</b>
				<span class="w3-small w3-text-black ">${place.price} euro</span> </span><br>
			<span class="w3-medium w3-text-theme "><b>Date of posting:</b>
				<span class="w3-small w3-text-black ">${place.dateOfPosting}
			</span> </span><br> <br> <input type="hidden" value="${place.id }"
				name="id" /> <a href="reservation/${place.id}"> <span
				class="w3-medium w3-text-highway-blue"><b>Make
						reservation</b></span>
			</a>
		</div>
	</div>
	
	

	<!-- Gallery -->
	<h2 style="text-align: center">Gallery</h2>
	<c:if test="${place.photosURLs.size() == 0 }">
		<h5>
			<b>&nbsp No images. </b>
		</h5>
		<br>
	</c:if>
	<div class="row">
		<c:forEach items="${place.photosURLs }" var="url" begin="0" end="4">
			<div class="column">	
				<img src="data:image/jpeg;base64,${url}" style="width: 80%"
					onclick="openModal();currentSlide(<%=count++ %>)"
					class="hover-shadow cursor">
			</div>
		</c:forEach>
	</div>

	<div id="myModal" class="modal">
		<span class="close cursor" onclick="closeModal()">&times;</span>
		<div class="modal-content">
			<c:forEach items="${place.photosURLs }" var="url" begin="0" end="3">
				<div class="mySlides">
					<div class="numbertext"><%=count2++%>
						/ 4
					</div>
					<img src="data:image/jpeg;base64,${url}" style="width: 100%">
				</div>


			</c:forEach>
			<a class="prev" onclick="plusSlides(-1)">&#10094;</a> <a class="next"
				onclick="plusSlides(1)">&#10095;</a>

			<div class="caption-container">
				<p id="caption"></p>
			</div>

			<c:forEach items="${place.photosURLs }" var="url" begin="0" end="3">
				<div class="column">
					<img class="demo cursor" src="data:image/jpeg;base64,${url}"
						style="width: 100%" onclick="currentSlide(<%=count3++ %>)"
						alt="place">
				</div>
			</c:forEach>
		</div>
	</div>
	<script>
		function openModal() {
			document.getElementById('myModal').style.display = "block";
		}

		function closeModal() {
			document.getElementById('myModal').style.display = "none";
		}

		var slideIndex = 1;
		showSlides(slideIndex);

		function plusSlides(n) {
			showSlides(slideIndex += n);
		}

		function currentSlide(n) {
			showSlides(slideIndex = n);
		}

		function showSlides(n) {
			var i;
			var slides = document.getElementsByClassName("mySlides");
			var dots = document.getElementsByClassName("demo");
			var captionText = document.getElementById("caption");
			if (n > slides.length) {
				slideIndex = 1
			}
			if (n < 1) {
				slideIndex = slides.length
			}
			for (i = 0; i < slides.length; i++) {
				slides[i].style.display = "none";
			}
			for (i = 0; i < dots.length; i++) {
				dots[i].className = dots[i].className.replace(" active", "");
			}
			slides[slideIndex - 1].style.display = "block";
			dots[slideIndex - 1].className += " active";
			captionText.innerHTML = dots[slideIndex - 1].alt;
		}
	</script>

</body>
</html>