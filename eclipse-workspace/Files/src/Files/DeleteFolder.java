package Files;

import java.io.File;

public class DeleteFolder {
public static void main(String[] args) {
	File file = new File("D:Anjali.txt"); 
    if (file.delete()) { 
      System.out.println("Deleted the folder: " + file.getName());
    } else {
      System.out.println("Failed to delete the folder.");
    } 
}
}
