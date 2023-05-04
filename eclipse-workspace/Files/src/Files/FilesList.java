package Files;

import java.io.File;

public class FilesList {
public static void main(String[] args) {
	int count=0;//count no of files
	File file = new File("C:\\Program Files");
	String[] s=file.list();
	for(String s1:s)
	{
		count++;
		System.out.println(s1);
	}
	System.out.println(count);
	
}
}
