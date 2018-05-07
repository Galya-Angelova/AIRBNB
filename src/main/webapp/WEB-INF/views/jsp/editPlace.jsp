<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="navigation.jsp"></jsp:include>

<title>Places</title>
</head>
<body>
	<c:choose>
		<c:when test="${place != null}">
			<form action="editPlace" method="post">
				<div class="panel-body">
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_name"> Name
						</label>
						<div class="col-sm-9">

							<input id="placeName" name="name" size="30" type="text"
								value="${place.name}" /> <input id="id" name="id" type="hidden"
								value="${place.id }" />
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
					</div> --%><div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_city">
							City </label>
						<div class="col-sm-9">

							<input id="city" name="city" size="30" type="text"
								value="${place.city}" />
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_country">
							Country </label>
						<div class="col-sm-9">

							<input id="country" name="country" size="30" type="text"
								value="${place.country}" />
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_street">
							Street </label>
						<div class="col-sm-9">
							<input type="text" name="street" size="30" value="${place.street }"
								 required>
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_street_number">
							Street number </label>
						<div class="col-sm-9">
							<input type="number" name="streetNumber" min = "1" size="30" value = "${place.streetNumber }">
						</div>
					</div>
					<div class="row row-condensed space-4">
						<label class="text-right col-sm-3" for="place_type_name">
							Place type:  </label>
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
					<input type="hidden" name="dateOfPosting" value="${place.dateOfPosting }" />
					<!-- <div class="row row-condensed space-4">
				<div class="col-sm-9">
					<input type="submit" name="updateProfile" value="Update profile">
				</div>
			</div> -->
					<button type="submit" class="btn btn-primary btn-large">
						Save</button>
					<!-- <br> <input type="submit" value="SaveProfil"> <br> -->
				</div>
			</form>

		</c:when>
		<%-- <c:otherwise>
			
		</c:otherwise> --%>
	</c:choose>
	<br>

</body>
</html>