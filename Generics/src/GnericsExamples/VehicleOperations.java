package GnericsExamples;

import java.util.ArrayList;

public class VehicleOperations {
public static void main(String[] args) {
	ArrayList<String> a=new ArrayList<String>();
	a.add("BMW");
	a.add("Ferrari");
	a.add("Honda");
	Car<String> c=new Car<String>();
	c.start("Start");
	c.stop(a);
}
}
