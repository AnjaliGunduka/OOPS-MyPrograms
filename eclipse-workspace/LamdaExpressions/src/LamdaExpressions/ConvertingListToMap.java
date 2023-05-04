package LamdaExpressions;

public class ConvertingListToMap {
	private Integer key;

	private String value; // value will be the value of the above key

	public Integer getKey() {
		return key;
	}


	public String getValue() {
		return value;
	}

	public ConvertingListToMap(Integer id, String name) {
		super();
		this.key = id;
		this.value = name;
	}
}
