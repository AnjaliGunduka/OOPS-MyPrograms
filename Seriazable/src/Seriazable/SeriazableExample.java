package Seriazable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SeriazableExample implements Serializable {
	int i;
	String s;

	public SeriazableExample(int i, String s) {
		super();
		this.i = i;
		this.s = s;
	}

	static class Example {
		public static void main(String[] args) throws IOException, ClassNotFoundException {

			SeriazableExample a = new SeriazableExample(20, "Anjali");//class object

			FileOutputStream fos = new FileOutputStream("D:Anusha.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(a);
			// De-serializing 'a'
			FileInputStream fis = new FileInputStream("D:Anusha.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			SeriazableExample b = (SeriazableExample) ois.readObject(); // down-casting object

			System.out.println(b.i + " " + b.s);
			// closing streams
			oos.close();
			ois.close();

		}
	}
}
