<html>
<head>
<meta http-equiv=<em>
<title>Add New Stock Product</title>
</head>
<body>
<div align=<em>
<form  action="addproduct"</em>>
<table
	style="align: <em>center</em>; background-color:<em>#F1E6E6</em>; border=0; cellpadding=5">
	<caption>
		<h2>New Product</h2>
	</caption>
	<tr>
		<td>Enter Product Name:</td>
		<td><input type="text"></td>
	</tr>

	<tr>
		<td>Enter Product Price:</td>
		<td><input type="text"></td>
	</tr>

	<tr>
		<td>Enter Product Category:</td>
		<td><input type="text"></td>
	</tr>

</table>

<input type="submit" value="Add Product">

</form>

<c:url var="back_home" value="backtoindex"/>

<a href=" ${back_home}"> <u><</u>< Return to Home Page
</a>

</div>
</body>

</html>