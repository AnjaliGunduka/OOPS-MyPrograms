<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h2>
		<%
		if (session.getAttribute("login") == null || session.getAttribute("login") == "") {
			response.sendRedirect("Index.jsp");
		}
		%>
		welcome,
		<%=session.getAttribute("login")%>
	</h2>
	<h2>
		<a href="logout.jsp">Logout</a>
	</h2>
</body>
</html>