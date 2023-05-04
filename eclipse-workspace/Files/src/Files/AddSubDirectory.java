package Files;

import java.io.File;

public class AddSubDirectory {
public static void main(String[] args) {
	try
	{
		File f=new File("D:\\AnjaliGunduka/Anjali.txt");
		boolean done=f.createNewFile();
		System.out.println(done);
	}catch(Exception e) {
		System.out.println("error");
	}
}
}
