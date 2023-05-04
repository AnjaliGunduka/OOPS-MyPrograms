package StreamsApi;

import java.util.function.Function;

public class FunctionalInterface {
	public static void main(String[] args) {
		// Square Root Using Functional Interface
		Function<Integer, Double> squareRoot = (a) -> {
			return Math.sqrt(a);
		};
		int a = 4;
		int b = 4;
		double r = squareRoot.apply(a);
		System.out.println(r);

		// Square Root Multiplication Using Functional Interface
		// lambda expression to define the calculate method
		multiplication f = (int x) -> (x * x);
		int s = f.calcuator(a);
		System.out.println(s);
		// Square Root Multiplication Using Functional Interface
		// lambda expression to define the calculate method
		multiplicationFunctionalInterface multiplicationFunctionalInterface = (int num1, int num2) -> (num1 + num2);
		int s1 = multiplicationFunctionalInterface.sum(a, b);
		System.out.println(s1);

		Function<Integer, Double> half = ab -> ab * 2.0;

		// Now treble the output of half function
		half = half.andThen(ab -> 6 * ab);
		System.out.println(half.apply(10));

		SumFunctionalInterface sumFunctionalInterfaces = new SumFunctionalInterface() {
			@Override
			public int sum(int num1, int num2) {
				return num1 + num2;
			}
		};
		System.out.println(sumFunctionalInterfaces.sum(6, 2));

	}

}

interface multiplicationFunctionalInterface {
	int sum(int num1, int num2);

}

interface SumFunctionalInterface {

	int sum(int num1, int num2);

}

interface multiplication {
	int calcuator(int x);

}
