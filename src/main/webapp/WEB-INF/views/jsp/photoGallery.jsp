<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Gallery -->

	<h2 style="text-align: center">Gallery</h2>
	<c:if test="${place.photosURLs.size() == 0 }">
		<h5 style="text-align: center">
			<b> No images. </b>
		</h5>
		<br>
	</c:if>
	<div class="row">
		<c:forEach items="${place.photosURLs }" var="url" begin="0" end="4" varStatus="loop">
			<div class="column">	
				<img src="data:image/jpeg;base64,${url}" style="width: 80%"
					onclick="openModal();currentSlide(${loop.index})"
					class="hover-shadow cursor">
			</div>
		</c:forEach>
	</div>

	<div id="myModal" class="modal">
		<span class="close cursor" onclick="closeModal()">&times;</span>
		<div class="modal-content">
			<c:forEach items="${place.photosURLs }" var="url" begin="0" end="3" >
				<div class="mySlides">
					
					<img src="data:image/jpeg;base64,${url}" style="width: 100%">
				</div>


			</c:forEach>
			<a class="prev" onclick="plusSlides(-1)">&#10094;</a> <a class="next"
				onclick="plusSlides(1)">&#10095;</a>

			<div class="caption-container">
				<p id="caption"></p>
			</div>

			<c:forEach items="${place.photosURLs }" var="url" begin="0" end="3" varStatus="loop">
				<div class="column">
					<img class="demo cursor" src="data:image/jpeg;base64,${url}"
						style="width: 100%" onclick="currentSlide(${loop.index})"
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