package Interfaces;

public class NonStaticFields {
 static int nonStatic;

public static void main(String[] args) {
	
	
NonStaticFields customer = new NonStaticFields();

customer.NonStaticFields(nonStatic);
System.out.println("valu of"+customer.nonStatic);
}

 void NonStaticFields(int nonStatic2) {
	// TODO Auto-generated method stub
	this.nonStatic=nonStatic;
}
}
