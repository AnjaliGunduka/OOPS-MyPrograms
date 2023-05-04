package GnericsExamples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorMain {
public static void main(String[] args) {
	List<CollgeComparator> col=new ArrayList<CollgeComparator>();
	col.add(new CollgeComparator(534,"Anjali","Cse"));
	col.add(new CollgeComparator(531,"neha","Cse"));
	col.add(new CollgeComparator(513,"Lalitha","Cse"));
	col.add(new CollgeComparator(510,"Preethi","Cse"));
	col.add(new CollgeComparator(550,"Nirosha","Cse"));

	Comparator<CollgeComparator> a=new Comparator<CollgeComparator>()
			{

				@Override
				public int compare(CollgeComparator o1, CollgeComparator o2) {
					// TODO Auto-generated method stub
					 if(o1.getId()>o2.getId())
						 return 1;
					 else
						 return -1;
				}
		
			};
	
	Collections.sort(col,a);
	
	
	
	for(CollgeComparator com:col)
	{
		System.out.println(com);
	}
	
	
}
}
