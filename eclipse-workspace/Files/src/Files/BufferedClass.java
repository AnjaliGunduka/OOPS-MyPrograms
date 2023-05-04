package Files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BufferedClass {
	public static void main(String[] args) throws IOException {
		FileReader filerea = new FileReader("D:Anusha.txt");//
		BufferedReader buff = new BufferedReader(filerea);
		int i;
		while ((i = buff.read()) != -1) {
			System.out.print((char) i);
		}
		buff.close();
		filerea.close();
	}

}
