package Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadTheData {
public static void main(String[] args) throws IOException {
	FileReader f1 = new FileReader("D:Anusha.txt");
     int i=f1.read();
     while(i!=-1)
     {
    	 System.out.println((char)i);//typecasting
    	 i=f1.read();
     }
	
}
}
