package Seriazable;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DeSerilizationExample {
	public static void main(String[] args) throws Exception {
		try {
			FileInputStream fis = new FileInputStream("my_data.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);

			Student stud2 = (Student) ois.readObject();
			System.out.println("The object has been deserialized.");

			fis.close();
			ois.close();

			System.out.println("Name = " + stud2.name);
			System.out.println("Department = " + stud2.id);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
