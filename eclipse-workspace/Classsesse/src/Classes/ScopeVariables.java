package Classes;

public class ScopeVariables {
	static int x = 8;
    private int y = 20;
    public void method1(int x)
    {
    	ScopeVariables var = new ScopeVariables();
        this.x =20;
        y = 88;
        System.out.println("Test.x: " + ScopeVariables.x);//this.x will peint here
        System.out.println("scopeVariable of x " + var.x);//x value will print 
        System.out.println("scopeVariable of y " + var.y);//y value
        System.out.println("y: " + this.y);
    }
 
    public static void main(String args[])
    {
    	ScopeVariables a1=new ScopeVariables();
    	a1.method1(8);
    }
}
