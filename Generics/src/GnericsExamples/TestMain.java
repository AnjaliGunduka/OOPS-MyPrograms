package GnericsExamples;

public class TestMain {
	public static void main(String[] args) {
		Test<Integer> iObj = new Test();
		System.out.println(iObj.getObject());

		// instance of String type
		Test<String> sObj = new Test();
		System.out.println(sObj.getObject());
	}
}
