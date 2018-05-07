<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<jsp:include page="navigation.jsp"></jsp:include>

<title>Awaiting reservations</title>
</head>
<body>
<div id="contents" class="w3-container menus w3-right"
		style="width: 70%; margin-right: 4%">
		<h1>Awaiting reservations</h1>
		<c:choose>
			<c:when test="${reservations.isEmpty()}">
				<h5>
					<b>There is no awaiting reservations now. </b>
				</h5>
				<br>
			</c:when>
			<c:otherwise>
				<c:forEach var="entry" items="${reservations}">
					<div class="w3-container w3-border w3-round-xxlarge w3-white"
						style="margin-top: 2%">
						<div class="w3-row-padding">

							<div class="w3-col w3-container w3-margin" style="width: 45%;">
								<span class="w3-medium w3-text-theme w3-text-theme">
										Reservation details: <br>
									</span> <span class="w3-medium w3-text-theme "><b>Start date:</b> <span
										class="w3-small w3-text-black ">${entry.key.startDate}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>End date:</b> <span
										class="w3-small w3-text-black ">${entry.key.endDate}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>Reservation was made on:</b> <span
										class="w3-small w3-text-black ">${entry.key.reservationDate}</span> </span><br>
										<input type="hidden" value="${entry.key.id }"
										name="id" /> <a href="reservations/${entry.key.id}"> <span
										class="w3-medium w3-text-highway-blue"><b>Reject reservation</b></span>
									</a>
							</div>
							<div class="w3-col w3-container w3-margin" style="width: 45%;">

								<div class="w3-container">
								<a href="<c:out value="${URL}"/>"> <span
										class="w3-large w3-text-highway-blue"><b>${entry.value.name}</b></span><br>
										<br>
									</a> 
									<span class="w3-medium w3-text-theme w3-text-theme">
										Address: <br>
									</span> <span class="w3-medium w3-text-theme "><b>City:</b> <span
										class="w3-small w3-text-black ">${entry.value.city}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>Country:</b> <span
										class="w3-small w3-text-black ">${entry.value.country}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>Street:</b> <span
										class="w3-small w3-text-black ">${entry.value.street}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b>Street
											number:</b> <span class="w3-small w3-text-black ">${entry.value.streetNumber}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b>Place
											type:</b> <span class="w3-small w3-text-black ">${entry.value.placeTypeName}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b>Price
											for night:</b> <span class="w3-small w3-text-black ">${entry.value.price}
											euro</span> </span><br> 
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