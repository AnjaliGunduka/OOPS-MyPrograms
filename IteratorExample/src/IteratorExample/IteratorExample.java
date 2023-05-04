package IteratorExample;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IteratorExample {
	public static void main(String[] args) {
		List<Integer> data = new LinkedList<Integer>();
		data.add(31);
		data.add(21);
		data.add(41);
		data.add(51);
		data.add(11);
		data.add(81);
		System.out.println("data: " + data);

		Iterator i = data.iterator();
		System.out.println("The NewData values are: ");
		while (i.hasNext()) {
			System.out.println(i.next());
		}

		for (Integer i1 : data) {
			System.out.println("loop pf the data is:" + i1);
		}
	}
}
