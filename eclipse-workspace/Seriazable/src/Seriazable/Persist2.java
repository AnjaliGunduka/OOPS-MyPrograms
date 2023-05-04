package Seriazable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Persist2 {
	public static void main(String args[]) {
		try {
			// Creating the object
			Student s1 = new Student();
			s1.id=534;
			s1.name="Anjali";
			s1.address="Dharpally";
//			System.out.println(s1);
			// Creating stream and writing the object
			FileOutputStream fout = new FileOutputStream("D:Book.txt");
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(s1);
			//out.flush();
			// closing the streamss
			FileInputStream fis = new FileInputStream("D:Book.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Student b = (Student) ois.readObject(); 
			System.out.println(b.id + " " + b.name);
			
			out.close();
			fout.close();
			System.out.println("success");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

