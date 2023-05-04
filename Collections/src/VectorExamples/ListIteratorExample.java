package VectorExamples;

import java.util.ListIterator;
import java.util.Vector;

public class ListIteratorExample {
public static void main(String[] args) {
	Vector<String> vector = new Vector<String>();
	 
    //Adding elements to the Vector
    vector.add("Item1");
    vector.add("Item2");
    vector.add("Item3");
    vector.add("Item4");
    vector.add("Item5");

    ListIterator litr = vector.listIterator();
    System.out.println("Traversing in Forward Direction:");
    while(litr.hasNext())
    {
      System.out.println(litr.next());
    }

    System.out.println("\nTraversing in Backward Direction:");
    while(litr.hasPrevious())
    {
      System.out.println(litr.previous());
    }
 
}
}
