package GnericsExamples;

import java.util.ArrayList;

public class Car<T> implements Vehicle<T> {

	@Override
	public void start(T e) {
		// TODO Auto-generated method stub
		System.out.println("Start the car");
	}

	@Override
	public void stop(ArrayList<? extends T> e) {
		// TODO Auto-generated method stub
		System.out.println("stop the car");
	}

	public String get(String index) {
		return String.valueOf(index);
	}
}
