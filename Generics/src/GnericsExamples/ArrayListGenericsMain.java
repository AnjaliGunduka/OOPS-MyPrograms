package GnericsExamples;

public class ArrayListGenericsMain {
	public static void main(String[] args) {
		ArrayListGenerics<String> set1 = new ArrayListGenerics<String>();
		set1.add("Anjali");
		set1.add("Anusha");
		set1.add("SaiTeja");
		System.out.println(set1.contains("Anjali"));
		System.out.println(set1.contains("Anu"));

		ArrayListGenerics<Integer> set2 = new ArrayListGenerics<Integer>();
		set2.add(3);
		set2.add(55);
		System.out.println(set2.contains(20));
		System.out.println(set2.contains(55));
	}
}
