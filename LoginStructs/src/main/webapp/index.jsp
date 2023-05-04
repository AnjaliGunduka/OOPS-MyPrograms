
<%@ taglib uri="/struts-tags" prefix="s"%>
<body>
<h1>Login Application</h1>
<s:form action="verify">    
<s:textfield name="uname" label="Enter Your Name"><br></s:textfield>  
<s:textfield name="password" label="enter password"><br></s:textfield>  
<s:submit value="login" align="center"></s:submit>  
</s:form>  
</body>
</html>