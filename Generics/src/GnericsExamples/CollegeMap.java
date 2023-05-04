package GnericsExamples;

import java.util.HashMap;

public class CollegeMap<K,V> implements CollegeMapOperations{
K studnames;
V stuMarks;
private  HashMap<K,V>StuBranch;
public void add(K studnames,V stuMarks)
{
	System.out.println(studnames+""+stuMarks);
}
@Override
public String toString() {
	return "CollegeMap [studnames=" + studnames + ", stuMarks=" + stuMarks + "]";
}
public K getStudnames() {
	return studnames;
}
public void setStudnames(K studnames) {
	this.studnames = studnames;
}
public V getStuMarks() {
	return stuMarks;
}
public void setStuMarks(V stuMarks) {
	this.stuMarks = stuMarks;
}
@Override
public void studentNames() {
	// TODO Auto-generated method stub
	System.out.println("Students names are");
}
@Override
public void studentMarks() {
	// TODO Auto-generated method stub
	System.out.println("Students marks");
}

}
