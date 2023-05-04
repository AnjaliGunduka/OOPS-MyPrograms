package StreamsApi;

public class College {
long id;
String name;
double marks;
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
public double getMarks() {
	return marks;
}
@Override
public String toString() {
	return "College [id=" + id + ", name=" + name + ", marks=" + marks + "]";
}
public void setMarks(double marks) {
	this.marks = marks;
}
public College(long id, String name, double marks) {
	super();
	this.id = id;
	this.name = name;
	this.marks = marks;
}



}
