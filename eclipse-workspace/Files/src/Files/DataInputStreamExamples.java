package Files;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DataInputStreamExamples {
public static void main(String[] args) throws IOException {
	 InputStream input = new FileInputStream("D:\\testout.txt");  
	    DataInputStream inst = new DataInputStream(input);  
	    int count = input.available();  
	    byte[] ary = new byte[count];  
	    inst.read(ary);  
	    for (byte bt : ary) {  
	      char k = (char) bt;  
	      System.out.print(k+"-");  
	    }  
	  }  
}

