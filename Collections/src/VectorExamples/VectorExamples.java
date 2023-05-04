package VectorExamples;

import java.util.Vector;

public class VectorExamples {
	public static void main(String[] args) {
		Vector<String> names = new Vector<String>(2);
		names.addElement("anjali");
		names.addElement("anu");
		names.addElement("sai");
		names.addElement("deepu");
		names.addElement("Deepak");
		System.out.println(names);
		System.out.println("capacity of vector is" + names.capacity());
		System.out.println("size of a vector is:"+names.size());
		names.setSize(2);
		names.add(1, "element");
		System.out.println(names);
		 if(names.contains("Java"))  
         {  
              System.out.println("Java is present at the index " +names.indexOf("Java"));  
         }  
         else  
         {  
            System.out.println("Java is not present in the list");  
         }  
	}
}
