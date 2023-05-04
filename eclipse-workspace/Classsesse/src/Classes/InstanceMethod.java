package Classes;

public class InstanceMethod {
public static void main(String[] args) {
	InstanceMethod a=new InstanceMethod();
	System.out.println(a.add(8, 8));
}
protected int add(int a1,int b1)
{
	return a1+b1;
	
}
}
