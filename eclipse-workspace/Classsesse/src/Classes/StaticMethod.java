package Classes;

public class StaticMethod {
	String name;
	int id;
	static String names = "Anjali";

	public StaticMethod(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}

	static void display() {
		names = "AnjaliGunduka";
		System.out.println(names);
	}

	void displaying() {
		System.out.println("dispaly info" + name + "" + id + "" + names);
	}

	public static void main(String[] args) {
		StaticMethod a = new StaticMethod("Anjali", 534);
		System.out.println(a.id);
		System.out.println(a.name);
		StaticMethod.display();// if we want to print the static method use classname with staticmethod name
		a.displaying();
	}
}
