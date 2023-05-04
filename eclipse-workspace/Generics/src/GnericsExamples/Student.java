package GnericsExamples;

import java.util.Comparator;

public class Student<T> {
	T sId;
	T sName;

	@Override
	public String toString() {
		return "Student [sId=" + sId + ", sName=" + sName + "]";
	}

	public Student(T sId, T sName) {
		super();
		this.sId = sId;
		this.sName = sName;
	}
}

class IdComp<T> implements Comparator<Student<T>> {

	@Override
	public int compare(Student<T> s1, Student<T> s2) {
		// TODO Auto-generated method stub
		return ((String) s1.sId).compareTo((String) s2.sId);
	}

}