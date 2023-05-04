
<html>

<title>Stock Management System Demo</title>

<body>

	<h2>
		Welcome Stock Management System
	</h2>

	<div
		style=" text-align:center; margin-left:<em>30%</em>; background-color: <em>#F1E6E6</em>; width:<em>40%</em>">

		<c:url value="showadditionform" var="add_form" />

		<h2>
			<a href="${add_form}"> Add New Product</a>
		</h2>

		<br>

		<c:url value="getall" var="show_products" />

		<h2>
			<a href=" ${show_products} "> Display All
				Products</a>
		</h2>
	</div>
</body>

</html>