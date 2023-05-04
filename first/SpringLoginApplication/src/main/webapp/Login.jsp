<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head><title>Login</title></head>
<body>
<h1>Login</h1>
  <form action="userCheck" method="post">
        Enter UserName:<input type="text" name="username"><br>
        Enter Password:<input type="password" name="password"><br>
        <input type="submit" value="Login">  
    </form>
</body>
</html>