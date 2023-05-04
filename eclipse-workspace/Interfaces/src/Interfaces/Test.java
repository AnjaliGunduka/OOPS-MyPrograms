package Interfaces;

public class Test extends SuperTest implements Frob {
	    public static void main(String[] args) {
	        new Test().printV();
	    }
	    void printV() { 
	    	System.out.println((super.v + Frob.v)/2);
}
}
