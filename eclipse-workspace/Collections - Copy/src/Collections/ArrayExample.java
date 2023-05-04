package Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayExample {
	public static void main(String[] args) {
		String names[] = { "Anjali", "Anu", "sai", "Deepu" }; // create an array of string objs
	List name = new ArrayList(Arrays.asList(names));//
		Collections.sort(name);
		//for(String a:names)
		System.out.println(name);
	     
		
	}
}
