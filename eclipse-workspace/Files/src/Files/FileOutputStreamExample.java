package Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;

public class FileOutputStreamExample {
	public static void main(String[] args) throws IOException {
		File data = new File("D:\\testout.txt");
		FileOutputStream file = new FileOutputStream(data);
		FilterOutputStream filter = new FilterOutputStream(file);
		String s = "Welcome to javaTpoint.";
		byte b[] = s.getBytes();
		filter.write(b);
		filter.flush();
		filter.close();
		file.close();
		System.out.println("printed"+file);
	}
}
