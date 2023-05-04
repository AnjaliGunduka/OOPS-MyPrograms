package LamdaExpressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConverthingListToMapMain {
	public static void main(String[] args) {
		List<ConvertingListToMap> list = new ArrayList<ConvertingListToMap>();
		list.add(new ConvertingListToMap(1, "Anjali"));
		list.add(new ConvertingListToMap(2, "Anusha"));
		list.add(new ConvertingListToMap(3, "Neha"));
		list.add(new ConvertingListToMap(4, "Preethi"));
		list.add(new ConvertingListToMap(5, "Lalli"));

		Map<Integer, String> map = new HashMap<>();
		
		list.forEach((n) -> {
			map.put(n.getKey(), n.getValue());
		});
		System.out.println("Students id and  names "+map);
	}

}
