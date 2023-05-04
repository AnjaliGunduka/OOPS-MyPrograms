package GnericsExamples;

public class College<T>{ //implements Comparable<College<T>> {
	T stuId;
	T stuNames;
	T stuBranch;

	public College(T stuId, T stuNames, T stuBranch) {
		super();
		this.stuId = stuId;
		this.stuNames = stuNames;
		this.stuBranch = stuBranch;
	}

	@Override
	public String toString() {
		return "College [stuId=" + stuId + ", stuNames=" + stuNames + ", stuBranch=" + stuBranch + "]";
	}

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

//	@Override
//	public int compareTo(College<T> arg0) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stuBranch == null) ? 0 : stuBranch.hashCode());
		result = prime * result + ((stuId == null) ? 0 : stuId.hashCode());
		result = prime * result + ((stuNames == null) ? 0 : stuNames.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		College other = (College) obj;
		if (stuBranch == null) {
			if (other.stuBranch != null)
				return false;
		} else if (!stuBranch.equals(other.stuBranch))
			return false;
		if (stuId == null) {
			if (other.stuId != null)
				return false;
		} else if (!stuId.equals(other.stuId))
			return false;
		if (stuNames == null) {
			if (other.stuNames != null)
				return false;
		} else if (!stuNames.equals(other.stuNames))
			return false;
		return true;
	}

//	@Override
//	public int compareTo(College<T> clg) {
//		// TODO Auto-generated method stub
////		if (((String) this.getStuNames()).startsWith((String) getStuNames())) {
////			return 1;
////		} else {
////			return -1;
////		}
//		if (((String) this.getStuNames()).((String) getStuNames())) {
//			return 1;
//		} else {
//			return -1;
//		}

//		 if (this.getStuId() clg.getStuId()<T)
//				return 1;
//			else
//				return -1;
		
		
	}

