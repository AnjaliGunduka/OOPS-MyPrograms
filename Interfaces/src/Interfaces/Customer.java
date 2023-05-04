package Interfaces;

class Customer {
	static String staticField1;
public static void main(String[] args) {
	

Customer.staticField1 = "value";

System.out.println(Customer.staticField1);
}
}