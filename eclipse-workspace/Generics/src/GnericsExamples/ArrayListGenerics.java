package GnericsExamples;

import java.util.ArrayList;

public class ArrayListGenerics<T> {// Add <T> here to define a generic class 

	private ArrayList<T> names;

	public ArrayListGenerics() {
		names = new ArrayList<T>();
	}

	public void add(T item) // Use T for the generic data type
	{
		if (!names.contains(item))
			names.add(item);
	}

	public boolean contains(T item) {
		return (names.contains(item));
	}

	public void remove(T item) {
		names.remove(item);
	}
}
