package Files;

import java.io.File;

public class CreateAFile {
public static void main(String[] args) {
	try
	{
		File f=new File("D:\\Anjali.txt");
		if(f.createNewFile())
		{
			System.out.println("file created:-"+f);
		}
		else
		{
			System.out.println("already present");
		}
		
	}catch(Exception e) {
		System.out.println("error");
	}
}
}
