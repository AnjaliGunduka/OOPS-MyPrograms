package GnericsExamples;

class GenericExamples<T> {
	T value;

	public void show() {
		System.out.println(value.getClass().getName());
	}
}

class demo {
	public static void main(String[] args) {
		GenericExamples<Integer> a = new GenericExamples<Integer>();
		a.value=3;
		a.show();
	}
}
