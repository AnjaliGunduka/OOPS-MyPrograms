<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>Edit Student</h1>
<form:form method="POST" action="/SpringMVC_HibernateCRUD/editsave"
	modelAttribute="student">
	<table>
		<tr>
			<td><form:hidden path="studId" /></td>
		</tr>
		<tr>
			<td><label for="studName"></label>Student Name :</td>
			<td><form:input path="studName" /></td>
		</tr>
		<tr>
			<td><label for="marks"></label>Marks :</td>
			<td><form:input path="marks" /></td>
		</tr>

		<tr>
			<td><input type="submit" value="Edit Save" /></td>
		</tr>
	</table>
</form:form>
