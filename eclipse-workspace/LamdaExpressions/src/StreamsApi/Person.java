package StreamsApi;

public class Person {
	private String name;
	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Person(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
}
//Comparator class
class ComparisonProvider {
 // A method to compare with name
 public int compareByName(Person a, Person b)
 {
     return a.getName().compareTo(b.getName());
 }

 // A method to compare with age
 public int compareByAge(Person a, Person b)
 {
     return a.getAge().compareTo(b.getAge());
 }
}

