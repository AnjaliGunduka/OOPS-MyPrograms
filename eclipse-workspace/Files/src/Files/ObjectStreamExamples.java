package Files;

import java.io.ObjectStreamClass;
import java.util.ArrayList;

public class ObjectStreamExamples {
	public static void main(String[] args) {
		// creating object stream class for Number
		ObjectStreamClass objStream = ObjectStreamClass.lookup(Number.class);
		ObjectStreamClass objStream1 = ObjectStreamClass.lookupAny(ArrayList.class);

		// checking class instance
		System.out.println(objStream.forClass());
		System.out.println(objStream1.forClass());

	}
}
