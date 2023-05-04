package Files;

import java.io.ObjectStreamClass;

public class ObjectStreamFiledExample {
	public static void main(String[] args) {
		// creating object stream class for Number
		ObjectStreamClass geeks_stream = ObjectStreamClass.lookup(Number.class);

		// checking field
		System.out.println(geeks_stream.getField("quiz_stream"));
		System.out.println(geeks_stream.getFields());

		// class name
		System.out.println("class name: " + geeks_stream.getClass());

		// chheking serial version UID
		System.out.println(geeks_stream.getSerialVersionUID());

		System.out.println(geeks_stream.toString());
	}
}
