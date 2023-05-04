package TreeSet;

import java.util.TreeSet;

public class CloneExample {
public static void main(String[] args) {
	TreeSet<String> tree = new TreeSet<String>();
	  
    // Use add() method to add elements into the Set
    tree.add("Welcome");
    tree.add("To");
    tree.add("Geeks");
    tree.add("4");
    tree.add("Geeks");
    tree.add("TreeSet");

    // Displaying the TreeSet
    System.out.println("TreeSet: " + tree);

    // Creating a new cloned set
    TreeSet cloned_set = new TreeSet();

    // Cloning the set using clone() method
    cloned_set = (TreeSet)tree.clone();

    // Displaying the cloned_set
    System.out.println("The cloned TreeSet: " + cloned_set);
}
}
