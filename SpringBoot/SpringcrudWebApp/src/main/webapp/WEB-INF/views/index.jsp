<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<c:remove var="ename" scope="session" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Employee | Home</title>
<!-- Main css -->
<link rel="stylesheet" href="/css/style.css">
</head>
<body><br> <br> <br>
	<center>
		<h3>Spring Boot CRUD Example</h3>
	</center>
	<!-- Sign up form -->
	<section class="signup">
		<div class="container">
			<br>
			<center>
				<h2>Employee Management System</h2>
			</center>
			<div class="signup-image">

				
				<br>
				<br>
				<div style="display: flex; justify-content: space-between;">
					<a href="${pageContext.request.contextPath}/employee/sign-in" class="signup-image-link">Login</a> 
					<a href="${pageContext.request.contextPath}/employee/sign-up" class="signup-image-link">Register</a>
					<a href="${pageContext.request.contextPath}/employee/employees" class="signup-image-link">View Students</a>
				</div>
			</div>
			<br> <br> <br>
		</div>
	</section>
</body>
</html>