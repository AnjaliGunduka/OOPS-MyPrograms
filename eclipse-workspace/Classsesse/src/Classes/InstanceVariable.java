package Classes;

public class InstanceVariable {
private int i=80;
public void add()
{
	int i=88;// This local variable hides instance variable
	System.out.println("Value of instance varaibale "+this.i);
	System.out.println("added the local variable"+i);
}
public static void main(String[] args) {
	InstanceVariable a=new InstanceVariable();
	a.add();
}

}
