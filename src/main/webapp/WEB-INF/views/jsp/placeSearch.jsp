<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="navigation.jsp"></jsp:include>

<title>Search</title>
<body>
	<h3>Search</h3>
	<br>
<div id="filters" class="w3-container w3-white w3-round-xxlarge menus w3-left" style="margin-left:2%; width:20%">
	<form:form modelAttribute="editedFilter">
	
	<div class="w3-panel">
		<input class ="w3-margin"  type="submit" value="Filter" />
        <button type = "button" onclick="openOrCloseSection('orderingSection')" class="w3-small w3-btn w3-block w3-theme-d6  w3-left-align">Ordering</button>
    	<div id="orderingSection" class="w3-container w3-show w3-padding ">
			<form:select path="orderedBy" id="orderedBySelector" class="w3-select w3-border w3-round-xxlarge" name="option">
			  <form:option value="name">Alphabetically</form:option>
			  <form:option value="price">By Price</form:option>
			  <form:option value="city">By city(alphabetically)</form:option>
			  <form:option value="placeType">By place type</form:option>
			</form:select>
			<form:radiobutton path="isAscending" value="true"/> Ascending<br>
  			<form:radiobutton path="isAscending" value="false"/> Descending<br>
    	</div>
    </div>
	
	<div class="w3-panel">
        <button type = "button" onclick="openOrCloseSection('placeNameSection')" class="w3-small w3-btn w3-block w3-theme-d6  w3-left-align">Place Name</button>
    	<div id="placeNameSection" class="w3-container w3-show w3-padding ">
			<form:input id="filterName" path = "placeName" name="filterName" class="w3-container w3-input" type="text" maxlength="100" placeholder="Enter place name"/>
    	</div>
    </div>

	<div class="w3-panel">
        <button type = "button" onclick="openOrCloseSection('cityNameSection')" class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align">City Name</button>
    	<div id="cityNameSection" class="w3-container w3-show  w3-padding ">
    		 <form:select path="city" >
   				 <form:options items="${cities}" />
  			</form:select>
    	</div>
    </div>

    <div class="w3-panel">
        <button type = "button" onclick="openOrCloseSection('priceSection')" class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align">Price</button>
    	<div id="priceSection" class="w3-container w3-show  w3-padding ">
				<div class="slidecontainer">
                <span>Min: <span id="minPriceForNight">${editedFilter.minPriceForNight}</span></span>
  				<form:input path="minPriceForNight" type="range" min="${filter.minPriceForNight}" max="${filter.maxPriceForNight}" value="${editedFilter.minPriceForNight}" class="slider" id="minPriceSlider" 
  					oninput="getSliderValue('minPriceSlider','minPriceForNight')"/>
				</div>
                <div class="slidecontainer">
                <span>Max: <span id="maxPriceForNight">${editedFilter.maxPriceForNight}</span></span>
  				<form:input path="maxPriceForNight" type="range" min="${filter.minPriceForNight}" max="${filter.maxPriceForNight}" value="${editedFilter.maxPriceForNight}" class="slider" id="maxPriceSlider" 
  					oninput="getSliderValue('maxPriceSlider','maxPriceForNight')"/>
				</div>	
    	</div>
    </div> 
    
	<div class="w3-panel">
        <button type = "button" onclick="openOrCloseSection('placeTypesSection')" class="w3-small w3-container w3-btn w3-block w3-theme-d6 w3-left-align">Place Type</button>
    	<div id="placeTypesSection" class="w3-container w3-show  w3-padding ">
    		<form:checkboxes path="placeTypes" items="${placeTypes}" delimiter="<br/>"/>
    	</div>
    </div>
	</form:form>
</div>

<!-- Products section -->
<div id ="contents" class="w3-container menus w3-right" style="width:70%; margin-right:4%">
	<h1>Filtered products</h1>
	<c:forEach var="place" items="${places}">
	<div class="w3-container w3-border w3-round-xxlarge w3-white" style="margin-top:2%">
		<div class="w3-row-padding">
			<div class="w3-col w3-container w3-margin" style="width:25%;">
				<div class="w3-container w3-padding">
	        		<a href="place/${place.id}">
	        			<img title="${place.name}" class = "w3-round" style="width:100%; height:225px;">
	        			<!-- <img src="getPic?pic=${place.mainPic}" title="${place.name}" class = "w3-round" style="width:100%; height:225px;">  -->
	        		</a>
	        	</div>
	        </div>
	        	<div class="w3-col w3-container w3-margin" style="width:45%;">
	            	<div class="w3-container">
	                  <a href="place/${place.id}">
	                  	<span class="w3-large w3-text-highway-blue"><b>${place.name}</b></span><br><br>
	                  </a>
	                  
	                  <span class="w3-medium w3-text-theme " ><b>Place type:</b>
	                  	<span class="w3-small w3-text-black ">${place.placeType.name}</span>
	                  </span><br>
					  <span class="w3-medium w3-text-theme "><b>Address id (in future- City):</b>
					  	<span class="w3-small w3-text-black ">${place.addressID}</span>
					  </span><br>
					<!--   <span class="w3-medium w3-text-theme "><b>Rating:</b>
					  	<div class="w3-tag w3-round w3-green w3-tiny" style="padding:3px">
							<div class="w3-tag w3-round w3-green w3-border w3-border-white">
								${place.Rating}
							</div>
						</div>
					  </span><br>-->
					 
					  <span class="w3-medium w3-text-theme "><b>Address id (in future- Country):</b>
					  	<span class="w3-small w3-text-black ">${place.addressID}</span>
					  </span><br>
					  
	              </div>
	          </div>
	          <div class="w3-rest w3-container w3-margin">
	          	<span class="w3-medium w3-text-theme w3-text-black" > Pricing
	          	<br><br>
	          	


	        <!--    	<span class="w3-medium w3-text-black" >Buying</span>
				<c:choose>
				<c:when test="${product.originalBuyCost != product.buyCost }">
	          		<br><span class="w3-small w3-text-red">Old price: <del><fmt:formatNumber value="${product.originalBuyCost}" maxFractionDigits="2"/> <i class="fa fa-euro"></i></del></span><br>
	          		<span class="w3-medium">New price: <fmt:formatNumber value="${product.buyCost}" maxFractionDigits="2"/> <i class="fa fa-euro"></i></span>
	          	</c:when>
	          	<c:otherwise>
	          		<br><span class="w3-medium">Price: <fmt:formatNumber value="${product.buyCost}" maxFractionDigits="2"/> <i class="fa fa-euro"></i></span>
	          	</c:otherwise>
				</c:choose>-->
				
	          	
	          </div>
	      </div>
	  </div>
		</c:forEach>
	</div>
</div>
</body>
</html>