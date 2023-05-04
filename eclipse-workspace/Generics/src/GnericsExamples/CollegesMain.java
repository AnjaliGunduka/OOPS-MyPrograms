package GnericsExamples;

public class CollegesMain {
	public static void main(String[] args) {
		Colleges college1 = new Colleges();//object of a college
		college1.setStuId(534);
		college1.setStuNames("Anjali");
		college1.setStuBranch("cse");
//		System.out.println("Stuent id is" + college1.getStuId());
//		System.out.println("Students Names is" + college1.getStuNames());
//		System.out.println("Students Branch is" + college1.getStuBranch());
		Colleges<String> college2 = new Colleges<String>();
		// college1.setStuId(534);
		college2.setStuNames("Anju");
		college2.setStuBranch("Ece");
//		System.out.println("Students Names is" + college2.getStuNames());
//		System.out.println("Students Branch is" + college2.getStuBranch());
		Colleges clgArr[] = new Colleges[2];//Array format
		clgArr[0]=college1;
		clgArr[1]=college2;
		dispaly(clgArr);
	}
	public static <E> void dispaly(E[] elements) {
		for (E element : elements) {
			System.out.println(element);
		}
	}
}
