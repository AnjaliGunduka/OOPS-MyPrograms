package GnericsExamples;

public class Colleges<T>  {
	T stuId;
	T stuNames;
	T stuBranch;
	public T getStuId() {
		return stuId;
	}
	public void setStuId(T stuId) {
		this.stuId = stuId;
	}
	public T getStuNames() {
		return stuNames;
	}
	public void setStuNames(T stuNames) {
		this.stuNames = stuNames;
	}
	public T getStuBranch() {
		return stuBranch;
	}
	public void setStuBranch(T stuBranch) {
		this.stuBranch = stuBranch;
	}
	@Override
	public String toString() {
		return "Colleges [stuId=" + stuId + ", stuNames=" + stuNames + ", stuBranch=" + stuBranch + "]";
	}
}
