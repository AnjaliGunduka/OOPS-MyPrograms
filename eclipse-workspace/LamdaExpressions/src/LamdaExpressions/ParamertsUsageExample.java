package LamdaExpressions;

public class ParamertsUsageExample {
	public static void main(String[] args) {
//		SaySomething a = () -> {
//			return "Anju";//Zero Parameter use
//		};
//		SaySomething a1 = (water) -> {
//			return "give me some ";//one paramter
//		};
//
//		System.out.println(a1.ask("water"));
		
		SaySomething a1=(a,b)->(a+b);{
			System.out.println(a1.add(1, 2));//multiple parameters
		}
		
		
	}

}
