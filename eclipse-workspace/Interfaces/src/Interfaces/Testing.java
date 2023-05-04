package Interfaces;

public class Testing {
	public static int i = 0;

// constructor of class which counts
//the number of the objects of the class.
	Testing() {
		i++;

	}

// static method is used to access static members of the class 
// and for getting total no of objects 
// of the same class created so far
	public static int get() {
		// statements to be executed....
		return i;
	}

// Instance method calling object directly 
// that is created inside another class 'Abc'.
// Can also be called by object directly created in the same class 
// and from another method defined in the same class 
// and return integer value as return type is int.
	public int m1() {
		System.out.println("Inside the method m1 by object of GFG class");
		// calling m2() method
		this.m2();

		// statements to be executed if any
		return 1;
	}

// It doesn't return anything as 
// return type is 'void'.
	public void m2() {

		System.out.println("In method m2 came from method m1");
	}
}



