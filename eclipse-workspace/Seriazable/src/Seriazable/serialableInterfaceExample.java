package Seriazable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class serialableInterfaceExample implements Serializable {
public static void main(String[] args) {
	
	Student stud = new Student();
	stud.name="a";
	stud.id=534;
	
	try {
		FileOutputStream fos = new FileOutputStream("D:my_data.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(stud);
		oos.close();
		fos.close();
		System.out.println("The object has been saved to my_data file!");
	}
	catch(Exception e) {
		System.out.println(e);
    }
	}
}
