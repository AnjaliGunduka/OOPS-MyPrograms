package Collections;

import java.util.ArrayList;

public class ArrayListExamples {
	public static void main(String[] args) {
		ArrayList<Integer> arr = new ArrayList<Integer>(7);

		// using add() to initialize values
		arr.add(10);
		arr.add(20);
		arr.add(30);
		arr.add(40);
		arr.add(30);
		arr.add(30);
		arr.add(40);

		System.out.println("The list initially " + arr);

		int element = arr.lastIndexOf(40);//Last Index Number
		if (element != -1)//
			System.out.println("the lastIndexof of" + " 30 is " + element);
		else
			System.out.println("30 is not present in" + " the list");
	}
}
