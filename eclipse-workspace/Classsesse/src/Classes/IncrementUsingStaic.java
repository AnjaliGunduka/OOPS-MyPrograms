package Classes;

public class IncrementUsingStaic {
	static int count=8;//will get memory only once and retain its value  
	IncrementUsingStaic()
	{
		count++;
		System.out.println(count);
	}
	public static void main(String[] args) {
		IncrementUsingStaic a=new IncrementUsingStaic();
		IncrementUsingStaic qa1=new IncrementUsingStaic();
	}
}
