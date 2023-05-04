package GnericsExamples;

public class MinMaxOperations<T extends Comparable<T>> implements MinMax<T> {
	T[] values;

	public MinMaxOperations(T[] values) {
		super();
		this.values = values;
	}

	@Override
	public T min() {
		// TODO Auto-generated method stub
		T element = values[0];
		for (int i = 1; i < values.length; i++)

			if (values[i].compareTo(element) < 0) {

				element = values[i];
			}
		return element;

	}

	@Override
	public T max() {
		// TODO Auto-generated
		T element = values[0];
		for (int i = 1; i < values.length; i++)

			if (values[i].compareTo(element) > 0) {
				element = values[i];
			}
		return element;
	}

}
