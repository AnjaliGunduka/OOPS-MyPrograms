package Interfaces;

public class Point {

	static int x = 2;
}

class A extends Point {
static 	double x = 4.7;

	void printX() {
		System.out.println(x + " " + super.x);
	}
	public static void main(String[] args) {
		new A().printX();
	
	}

}