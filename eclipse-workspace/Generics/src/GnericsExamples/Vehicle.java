package GnericsExamples;

import java.util.ArrayList;

public interface Vehicle<T> {
	void start(T e);

	void stop(ArrayList<? extends T> e);

}
