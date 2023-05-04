<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head><title>Login</title></head>
<body>
<h1>Login</h1>
  <form:form action="userCheck" method="post">
   Enter User Name:<input type="text" name="name"> <br/>
   Enter Password :<input type="password" name="pwd"/><br/>
   <input type="submit">
  
  </form:form>
</body>
</html>