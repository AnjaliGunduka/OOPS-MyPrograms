package Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Example {
public static void main(String[] args) {
	
	 List<String> al=new ArrayList<String>();//creating arraylist    
	  al.add("Ravi");//adding object in arraylist    
	  al.add("Vijay");    
	  al.add("Ravi");    
	  al.add("Ajay");   
	  al.remove(0);
	    
	  List<String> al2=new LinkedList<String>();//creating linkedlist    
	  al2.add("James");//adding object in linkedlist    
	  al2.add("Serena");    
	  al2.add("Swati");    
	  al2.add("Junaid");  
	  al2.add(0,"anju");
	 // ((LinkedList<String>) al2).addLast("Mazda");
	    ((LinkedList<String>) al2).removeFirst();
	  System.out.println("arraylist: "+al);  
	  System.out.println("linkedlist: "+al2);  
	
	
}
}
