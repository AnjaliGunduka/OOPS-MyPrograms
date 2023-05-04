package TreeSet;

import java.util.Comparator;
import java.util.TreeSet;

public class ComparatorExampless implements Comparator<String> {

	public int compare(String str1, String str2) {
		String first;
		String second;
		first = str1;
		second = str2;
		return second.compareTo(first);
	}
}

class TreeDemo {
	public static void main(String[] args) {
		TreeSet<String> tree_set = new TreeSet<String>(new ComparatorExampless());

		tree_set.add("G");
		tree_set.add("E");
		tree_set.add("E");
		tree_set.add("K");
		tree_set.add("S");
		tree_set.add("4");
		System.out.println("Set before using the comparator: " + tree_set);

		System.out.println("The elements sorted in descending" + "order:");
		for (String element : tree_set)
			System.out.print(element + " ");
	}
}