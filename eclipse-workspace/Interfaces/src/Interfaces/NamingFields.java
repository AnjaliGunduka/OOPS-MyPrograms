package Interfaces;

public class NamingFields {
	static String staticField1;
	private String city;
	public static void main(String[] args) {
		NamingFields a=new NamingFields();
		a.city="new";
				System.out.println(a.city);
	}
}
