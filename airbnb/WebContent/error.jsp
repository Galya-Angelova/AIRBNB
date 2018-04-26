<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%Exception e = (Exception) request.getAttribute("exception");  %>
	<h1>Oops, something went wrong</h1>
	<h2>
		Reason :
		<%=e.getMessage()%></h2>
	<img src="images/error.jpg" />
	<br>
	<a href="index.jsp">Back to login</a>
</body>
</html>