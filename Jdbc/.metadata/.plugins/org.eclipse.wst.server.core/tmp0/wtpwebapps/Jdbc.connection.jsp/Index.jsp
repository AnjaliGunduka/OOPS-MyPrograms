<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<form action="LoginController" method="post" onsubmit="return validate();">
		UserName: <input type="text" name="txt_username"> <br> 
		Password: <input type="password" name="txt_password"> <br>
		
		<input type="submit" value="Login" name="btn_login"> <br>
		<h2><a href="Register.jsp">Register</a></h2>
	</form>

</body>
</html>