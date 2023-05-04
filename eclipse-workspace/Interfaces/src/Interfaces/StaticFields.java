package Interfaces;

public class StaticFields {
	int x, y, useCount;

	StaticFields(int x, int y) {
		this.x = x;
		this.y = y;
	}

	static final StaticFields aa1 = new StaticFields(0,0);
}
class Test1
{
public static void main(String[] args) {
	StaticFields p = new StaticFields(1,1);
	StaticFields q = new StaticFields(2,2);
    p.x = 3;
    p.y = 3;
    p.useCount++;
    p.aa1.useCount++;
    System.out.println("(" + q.x + "," + q.y + ")");
    System.out.println(q.useCount);
    System.out.println(q.aa1 == StaticFields.aa1);
    System.out.println(q.aa1.useCount);
}
}