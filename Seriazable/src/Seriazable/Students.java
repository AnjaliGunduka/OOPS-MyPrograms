package Seriazable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Students {
	private static final long serialVersionUID = 1L;
	String name;
	String className;
	String rollNo;

	public Students(String name, String className, String rollNo) {
		super();
		this.name = name;
		this.className = className;
		this.rollNo = rollNo;
	}

	public void names() {
		// TODO Auto-generated method stub
		System.out.println(name);
	}
}

class Col {
	public void objectSerialization(Students stu) {
		try {
			// Creating FileOutputStream object.
			FileOutputStream fos = new FileOutputStream("D:student.txt");

			// Creating ObjectOutputStream object.
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(stu);

			// close streams.
			oos.close();
			fos.close();

			System.out.println("Serialized data is saved in " + "D:\\New folder\\student.ser");
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}

class ex {
	public static void main(String[] args) {
		Students stu = new Students("Parmander", "MCA", "MCA/07/27");
		stu.names();
		
		
		Col col = new Col();
		col.objectSerialization(stu);
	}
}
