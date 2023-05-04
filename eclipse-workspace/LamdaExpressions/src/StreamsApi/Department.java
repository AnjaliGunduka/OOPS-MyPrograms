package StreamsApi;

public class Department implements Comparable<Department> {
	int id;
	String name;
	int marks;

	public Department(int id, String name, int marks) {
		super();
		this.id = id;
		this.name = name;
		this.marks = marks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", marks=" + marks + "]";
	}

	@Override
	public int compareTo(Department o) {
		// TODO Auto-generated method stub
		if (this.getId() > o.getId())
			return 1;
		else
			return -1;
	}

}
