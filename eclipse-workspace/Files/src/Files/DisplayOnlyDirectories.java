package Files;

import java.io.File;

public class DisplayOnlyDirectories {
	public static void main(String[] args) {
		int count = 0;
		File file = new File("C:\\Program Files");
		String[] s = file.list();
		for (String s1 : s) {
			File f1 = new File(file, s1);
			if (f1.isDirectory()) {
				count++;
				System.out.println(s1);
			}
		}
		System.out.println(count);
	}
}
