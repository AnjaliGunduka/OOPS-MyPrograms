package GnericsExamples;

import java.util.Arrays;
import java.util.List;

public class LoweBoundedExample {
public static void main(String[] args) {
	 //Lower Bounded Integer List
    List<Integer> list1= Arrays.asList(4,5,6,7);
      
    //Integer list object is being passed
    printOnlyIntegerClassorSuperClass(list1);

}

public static void printOnlyIntegerClassorSuperClass(List<? super Integer> list)
{
    System.out.println(list);
}
}

