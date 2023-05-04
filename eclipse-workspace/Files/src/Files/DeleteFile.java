package Files;

import java.io.File;

public class DeleteFile {
	public static void main(String[] args) {
		File file = new File("D:FileOperationExample.txt");
		if (file.delete()) {
			System.out.println(file.getName() + " file is deleted successfully.");
		} else {
			System.out.println("Unexpected error found in deletion of the file.");
		}
	}
}
