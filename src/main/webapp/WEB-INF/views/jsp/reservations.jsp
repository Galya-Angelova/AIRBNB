<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<jsp:include page="navigation.jsp"></jsp:include>

<title><sht:message code="reservations.title"/></title>
</head>
<body>
<div id="contents" class="w3-container menus w3-right"
		style="width: 70%; margin-right: 4%">
		<h1><sht:message code="reservations.title"/></h1>
		<c:choose>
			<c:when test="${reservations.isEmpty()}">
				<h5>
					<b><sht:message code="reservations.noReservations"/>. </b>
				</h5>
				<br>
			</c:when>
			<c:otherwise>
				<c:forEach var="entry" items="${reservations}" varStatus="loop">
					<div class="w3-container w3-border w3-round-xxlarge w3-white"
						style="margin-top: 2%">
						<div class="w3-row-padding">

							<div class="w3-col w3-container w3-margin" style="width: 45%;">
								<span class="w3-medium w3-text-theme w3-text-theme">
										<sht:message code="reservations.details"/>: <br>
									</span> 
									<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.id"/>:</b> <span
										class="w3-small w3-text-black ">${entry.key.id}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.start"/>:</b> <span
										class="w3-small w3-text-black ">${entry.key.startDate}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.end"/>:</b> <span
										class="w3-small w3-text-black ">${entry.key.endDate}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.madeOn"/>:</b> <span
										class="w3-small w3-text-black ">${entry.key.reservationDate}</span> </span><br>
										<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.guestName"/>:</b> <span
										class="w3-small w3-text-black ">${guests.get(loop.index).firstName} ${guests.get(loop.index).lastName}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.guestEmail"/>:</b> <span
										class="w3-small w3-text-black ">${guests.get(loop.index).email}</span> </span><br>
										<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.guestPhone"/>:</b> <span
										class="w3-small w3-text-black ">${guests.get(loop.index).phoneNumber}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="reservations.youHave"/> ${entry.key.daysForDeletion()} <sht:message code="reservations.days"/>:</b></span><br>
										<input type="hidden" value="${entry.key.id }"
										name="id" /> <a href="reservations/${entry.key.id}"> <span
										class="w3-medium w3-text-highway-blue"><b><sht:message code="reservations.reject"/></b></span>
									</a>
							</div>
							<div class="w3-col w3-container w3-margin" style="width: 45%;">
								<span class="w3-medium w3-text-theme w3-text-theme">
										<sht:message code="reservations.placeDetails"/>: <br></span> 
								<div class="w3-container">
								<input type="hidden" value="${entry.value.id }" name="id" />
									<c:url var="URL" value="placeDetails">
										<c:param name="id" value="${entry.value.id}" />
									</c:url>
									<a href="<c:out value="${URL}"/>"> <span
										class="w3-large w3-text-highway-blue"><b>${entry.value.name}</b></span><br>
										<br>
									</a>
									<span class="w3-medium w3-text-theme w3-text-theme">
										<sht:message code="myPlaces.address"/>: <br>
									</span> <span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.city"/>:</b> <span
										class="w3-small w3-text-black ">${entry.value.city}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.country"/>:</b> <span
										class="w3-small w3-text-black ">${entry.value.country}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.street"/>:</b> <span
										class="w3-small w3-text-black ">${entry.value.street}</span> </span><br>
									<span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.streetNumber"/>:</b> <span class="w3-small w3-text-black ">${entry.value.streetNumber}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b><sht:message code="addNewPlace.placeType"/>:</b> <span class="w3-small w3-text-black ">${entry.value.placeTypeName}</span>
									</span><br> <span class="w3-medium w3-text-theme "><b><sht:message code="myPlaces.priceNight"/>:</b> <span class="w3-small w3-text-black ">${entry.value.price}
											<sht:message code="myPlaces.euro"/></span> </span><br> 
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