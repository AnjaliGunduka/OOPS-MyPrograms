package Files;

import java.io.File;

public class FilePermissions {
	public static void main(String[] args) {
		File file = new File("D:Anjali.txt");

		// check if the file exists
		boolean exists = file.exists();
		if (exists == true) {
			
			file.setExecutable(true);
            file.setReadable(true);//file set to Readable
            file.setWritable(false);
            System.out.println("File permissions changed.");
  
			// printing the permissions associated with the file
			System.out.println("Executable: " + file.canExecute());
			System.out.println("Readable: " + file.canRead());
			System.out.println("Writable: " + file.canWrite());
		} else {
			System.out.println("File not found.");
		}
	}
}
