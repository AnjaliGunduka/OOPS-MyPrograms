package Classes;

public class Incrementingvalues {
	int a = 8;//not static print the same value

	public Incrementingvalues() {
		super();
		a++;// increment the value
		System.out.println(a);
	}

	public static void main(String[] args) {
		Incrementingvalues a1 = new Incrementingvalues();
	}
}
