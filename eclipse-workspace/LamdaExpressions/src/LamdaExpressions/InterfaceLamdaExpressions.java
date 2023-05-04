package LamdaExpressions;

import LamdaExpressions.Testing.FuncInter2;

public class InterfaceLamdaExpressions {
	interface fun1 {
		int add(int a, int b);//multiple paramters
	}

	interface fuc2 {
		void sendmsg(String message);//without parameters
	}

	private int operate(int a, int b, fun1 fobj) {
		return fobj.add(a, b);
	}

	public static void main(String[] args) {
		fun1 adding=(a,b)->a+b;
		InterfaceLamdaExpressions i1=new InterfaceLamdaExpressions();
		System.out.println(i1.operate(1, 2, adding));
		fuc2 fobj = message -> System.out.println("Hello " + message);
		fobj.sendmsg("world");
	

}
}
