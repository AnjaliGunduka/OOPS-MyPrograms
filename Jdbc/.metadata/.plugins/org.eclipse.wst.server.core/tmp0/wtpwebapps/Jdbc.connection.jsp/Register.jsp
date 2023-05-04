<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>REGISTRATION FORM</h2>

	<form action="RegisterController" method="post"
		onsubmit="return validate();">
		Name: <input type="text" name="txt_name"> <br> Moble No:
		<input type="text" name="txt_mobile"> <br> EMail: <input
			type="text" name="txt_email"> <br> Address: <input
			tye="text" name="txt_address"> <br> UserName: <input
			type="text" name="txt_username"> <br> Password: <input
			type="password" name="txt_password"> <br> <input
			type="submit" value="Register" name="btn_register"> <br>
		<h2>
			<a href="index.jsp">Login</a>
		</h2>
	</form>


</body>
</html>