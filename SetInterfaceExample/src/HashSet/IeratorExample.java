package HashSet;

import java.util.HashSet;
import java.util.Iterator;

public class IeratorExample {
	public static void main(String[] args) {
		HashSet<String> hs = new HashSet<String>();

		// Elements are added using add() method
		hs.add("Anju");
		hs.add("Anu");
		hs.add("Sai");
		hs.add("Deepu");
		hs.add("Anjali");
		hs.add("Gunduka");

		// Using enhanced for loop
		for (String s : hs)
			System.out.print(s + ", ");
		System.out.println();
	}
}
