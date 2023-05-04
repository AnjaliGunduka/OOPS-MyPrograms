
<html>
<head>
<meta charset="ISO-8859-1">
<title>SpringMVC</title>
</head>
<body>
 <div class="container">
  <div class="col-md-offset-2 col-md-7">
   <h2 class="text-center">Register</h2>
   <div class="panel panel-info">
    <div class="panel-heading">
     <div class="panel-title"><h2>Add Customer</h2></div>
    </div>
    <div class="panel-body">
     <form action="saveCustomer" cssClass="form-horizontal"
      method="post" modelAttribute="customer">

      <!-- need to associate this data with customer id -->
      <hidden path="id" />

      <div class="form-group">
       <label for="firstname" class="col-md-3 control-label">First
        Name</label>
       <div class="col-md-9">
        <input path="firstName" cssClass="form-control" />
       </div>
      </div>
      <div class="form-group">
       <label for="lastname" class="col-md-3 control-label">Last
        Name</label>
       <div class="col-md-9">
        <input path="lastName" cssClass="form-control" />
       </div>
      </div>

      <div class="form-group">
       <label for="email" class="col-md-3 control-label">Email</label>
       <div class="col-md-9">
        <input path="email" cssClass="form-control" />
       </div>
      </div>

      <div class="form-group">
       <!-- Button -->
       <div class="col-md-offset-3 col-md-9">
        <button cssClass="btn btn-primary">Submit<button>
       </div>
      </div>

     </form>
    </div>
   </div>
  </div>
 </div>
</body>
</html>