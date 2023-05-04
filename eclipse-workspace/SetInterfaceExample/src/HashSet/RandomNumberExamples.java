package HashSet;

import java.util.HashSet;
import java.util.Iterator;

public class RandomNumberExamples {
	public static void main(String[] args) {
		HashSet<String> Exam = new HashSet();
		Exam.add("One1");
		Exam.add("Two2");
		Exam.add("Three3");
		Exam.add("Four4");
		Exam.add("Five5");
		Iterator<String> i = Exam.iterator();
		while (i.hasNext()) {
			System.out.println(i.next());
		}
	}
}
