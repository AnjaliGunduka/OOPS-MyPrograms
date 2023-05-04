package Collections;

import java.util.ArrayList; // import the ArrayList class
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

public class ArrayListExample {
	public static void main(String[] args) {
		ArrayList<String> cars = new ArrayList<String>();// Create an ArrayList object
//		ArrayList<Integer> myNumbers = new ArrayList<Integer>();//we can use any number of arraylists
//		ArrayList<Integer,String> myNumbers1 = new ArrayList<Integer,String>();//it cannot give permission to two elements at a time
//		myNumbers.add(38);
//		myNumbers.add(83);
		cars.add("Volvo");
		cars.add("BMW");
		cars.add("Ford");
		cars.add("Mazda");
		System.out.println(cars);// To print all the elements in[]array
		System.out.println(cars.size());// Size of an array
//		System.out.println(myNumbers);
		Collections.sort(cars);// To print all the elements in Alphabetical or numerical
		for (int i = 0; i < cars.size(); i++) // To print all the elements in one line order
		{
			System.out.println(cars.get(i));
		}
		// for (String i : cars) {//For each tpo print the elements in one line
		// System.out.println(i);
		// }
		// for(int i:myNumbers) {
		// System.out.println(i);}
		// cars.remove(0);//remove an elements
		// System.out.println(cars);//to print afer removing elements
		// System.out.println(cars.get(0));//to access an one elements by using index
		// number
		 cars.set(0, "Opel");//to change first elements To modify an element, use the
		// set() method and refer to the index number:
		System.out.println(cars);//to print changed elements
		// Iterator itr=cars.iterator();
		// while(itr.hasNext()){
		// System.out.println(itr.next());
		// }
	Collections.sort(cars, Collections.reverseOrder());// to print in reverse order
		System.out.println(cars);

		ListIterator<String> iterator = cars.listIterator(2);//print the elements in next order
	System.out.println("\nUsing ListIterator:\n");
	

		while (iterator.hasNext()) {
			System.out.println("Value is : " + iterator.next());
		}
		
		cars.removeIf(n -> (n.charAt(0) == 'S'));
	System.out.println(cars);
		
    }
		

	}

