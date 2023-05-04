package LamdaExpressions;

import java.util.ArrayList;
import java.util.List;

public class LamdaBasicExample {
public static void main(String[] args) {
	List<Integer> list=new ArrayList<Integer>();
	list.add(34);
	list.add(1);
	list.add(8);
	list.forEach( (n) -> { System.out.println(n); } );
	
}
}
