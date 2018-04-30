<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@page isErrorPage="true"%>
 <jsp:include page="header.jsp"></jsp:include>
<title>Insert title here</title>
</head>
<body>
<%Exception e = (Exception) request.getAttribute("exception");  %>
	<h1>Oops, something went wrong</h1>
	<h2>
		Reason :
		<%=e.getMessage()%></h2>
	<img src="D:\\AIRBNB\\error.jpg" />
	<br>
	<a href="index">Back to login</a>
</body>
</html>