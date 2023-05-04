package Classes;

public class StaticVariable {

	int rollno;// instance variable
	static String name;// static variable
static int a1=8;
	public StaticVariable(int rollno) { //Constuctor
		super();
		this.rollno = rollno;
	}

	public static void main(String[] args) {
		StaticVariable a=new StaticVariable(534);
		name="Anjali";
		System.out.println(a1);
		System.out.println("My roll no is"+a.rollno);
		System.out.println("my name is"+name);
	}
}
