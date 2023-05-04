package GnericsExamples;

public class MinMaxMain {
public static void main(String[] args) {
	// Custom entries in an array
    Integer arr[] = { 3, 6, 2, 8, 6 };

    // Create an object of type as that of above class
    // by declaring Integer type objects, and
    // passing above array to constructor
    MinMaxOperations<Integer> minMaxValues = new MinMaxOperations<Integer>(arr);

    // Calling min() and max() methods over object, and

    // printing the minimum value from array elements
    System.out.println("Minimum value: " + minMaxValues.min());

    // printing the maximum value from array elements
    System.out.println("Maximum value: " + minMaxValues.max());
}
}
