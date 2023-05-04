package LamdaExpressions;

import java.util.Arrays;
import java.util.Comparator;

public class LamdaExpressionPractice {
	public static void main(String[] args) {

//By using implemented class
		Integer[] list2 = { 1, 6, 2, 7, 5 };
		Comparator<Integer> integerComparator = new IntegerComparator();
		Arrays.sort(list2, integerComparator);
		System.out.println(Arrays.toString(list2));
// By using anonymous class
		Integer[] list3 = { 1, 6, 2, 7, 5 };
		Arrays.sort(list3, new Comparator<Integer>() {
			public int compare(Integer a, Integer b) {
				return (b - a);
			}
		});
		System.out.println(Arrays.toString(list3));
// By using lamda
		Integer[] list1 = { 1, 6, 2, 7, 5 };
		Arrays.sort(list1, (a, b) -> b - a);
		System.out.println(Arrays.toString(list1));
	}
}