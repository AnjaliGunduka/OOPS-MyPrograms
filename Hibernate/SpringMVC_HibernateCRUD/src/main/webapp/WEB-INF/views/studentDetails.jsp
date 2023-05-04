
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>Students List</h1>
<table border="2" width="70%" cellpadding="2">
	<tr>
		<th>Id</th>
		<th>Student Name</th>
		<th>Marks</th>
		<th>Edit</th>
		<th>Delete</th>
	</tr>
	<c:forEach var="student" items="${listOfStudents}">
		<tr>
			<td>${student.studId}</td>
			<td>${student.studName}</td>
			<td>${student.marks}</td>
			<td><a href="updatestudent/${student.studId}">Edit</a></td>
			<td><a href="deletestudent/${student.studId}">Delete</a></td>
		</tr>
	</c:forEach>
</table>
<br />
<a href="studentform">Add New Student</a>