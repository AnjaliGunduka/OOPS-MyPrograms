package GnericsExamples;

public class ComparableExample implements Comparable<ComparableExample> {

	int id;
	String name;
	String branch;

	public ComparableExample(int id, String name, String branch) {
		super();
		this.id = id;
		this.name = name;
		this.branch = branch;
	}

	@Override
	public String toString() {
		return "CollgeComparable [id=" + id + ", name=" + name + ", branch=" + branch + "]";
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

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public int compareTo(ComparableExample clg) {
		// TODO Auto-generated method stub
//		if(this.getName().startsWith(getName()))	{
//			return 1;
//		}
//		else
//		{
//			return -1;
//		}
		 if (this.getId() > clg.getId())
			return 1;
		else
			return -1;
	}
}
