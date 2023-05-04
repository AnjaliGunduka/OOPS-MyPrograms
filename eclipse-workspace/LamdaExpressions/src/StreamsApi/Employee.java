package StreamsApi;

public class Employee {
long id;
String name;
double sal;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public double getSal() {
	return sal;
}
public void setSal(double sal) {
	this.sal = sal;
}
public Employee(long id, String name, double sal) {
	super();
	this.id = id;
	this.name = name;
	this.sal = sal;
}

}
