<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isErrorPage="true"%>
<%@ taglib prefix="sht" uri="http://www.springframework.org/tags" %>
<jsp:include page="header.jsp"></jsp:include>
<title><sht:message code="error.title"/></title>
</head>
<body>
	<%Exception e = (Exception) request.getAttribute("exception");  %>
	<h1><sht:message code="error.oops"/></h1>
	<h2>
		<sht:message code="error.reason"/> :
		<%=e.getMessage()%></h2>
	<img src="img/error.png" />
	<br>
	<%-- <c:url var="URL" value="index">
		<c:param name="param" value="${parameter}" />
	</c:url>
	<a href="<c:out value="${URL}"/>">Back to login</a> --%>
</body>
</html>