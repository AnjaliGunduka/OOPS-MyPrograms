<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Spring MVC - Login</title>
</head>
<body>
	<h2>Login</h2>
	<form method="post" action="processLogin">

		<table>
			<tr>
				<td><label path="userName">User Name</label></td>
				<td><input path="userName" /></td>
			</tr>
			<tr>
				<td><label path="password">Password</label></td>
				<td><input path="password" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Login" /></td>
			</tr>
		</table>

	</form>
</body>
</html>