package LamdaExpressions;

public class InterfaceExamples {
public static void main(String[] args) {
	Phone a=()->{
		return "IM IN CALL";
	};
	System.out.println(a.call());
	}
}

