package example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmployeeMain {
	public static void main(String[] args) throws ParseException {
		
		
		
		 //Reading name and date of birth from the user
//	      Scanner sc = new Scanner(System.in);
//	      System.out.println("Enter your name: ");
//	      String name = sc.next();
//	      System.out.println("Enter your date of birth (dd-MM-yyyy): ");
//	      String dob = sc.next();
//	      //Converting String to Date
//	      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//	      Date date = formatter.parse(dob);
//	      //Converting obtained Date object to LocalDate object
//	      Instant instant = date.toInstant();
//	      ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
//	      LocalDate givenDate = zone.toLocalDate();
//	      //Calculating the difference between given date to current date.
//	      Period period = Period.between(givenDate, LocalDate.now());
//	      
//	      System.out.print("Hello "+name+" your current age is: ");
//	      System.out.print(period.getYears()+" years "+period.getMonths()+" and "+period.getDays()+" days");
//	  
//		double avgSalary = EmployeeDatabase.getEmployees().stream()
//				.filter(employee -> employee.getGrade().equalsIgnoreCase("A")).map(employee -> employee.getSalary())
//				.mapToDouble(i -> i).average().getAsDouble();
//
//		System.out.println(avgSalary);
//
//		double sumSalary = EmployeeDatabase.getEmployees().stream()
//				.filter(employee -> employee.getGrade().equalsIgnoreCase("G")).map(employee -> employee.getSalary())
//				.mapToDouble(i -> i).sum();
//		System.out.println(sumSalary);
//
//		String namess = "JOHN";
//		List<Employee> names = EmployeeDatabase.getEmployees().stream()
//				.filter(employee -> employee.getName().equalsIgnoreCase(namess)).collect(Collectors.toList());
//		System.out.println(names);
////	        /**
////	         * Today Date
////	         */
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-EEE, MM, yyyy");
//		String stringDate = sdf.format(new Date());
//		System.out.println("Today is: " + stringDate);
//
//		// returns a Calendar object whose calendar fields have been initialized with
//		// the current date and time
//		Calendar cal = Calendar.getInstance();
//		// creating a constructor of the SimpleDateFormat class
//		SimpleDateFormat sdfs = new SimpleDateFormat("dd-MM-yyyy");
//		// getting current date
//		System.out.println("Today's date: " + sdfs.format(cal.getTime()));
//		// creating a constructor of the Format class
//		// displaying full-day name
//		Format f = new SimpleDateFormat("EEEE");
//		String str = f.format(new Date());
//		// prints day name
//		System.out.println("Day Name: " + str);
//
//	}
//
//	private static String getDayOfWeek(int value) {
//		String day = "";
//		switch (value) {
//		case 1:
//			day = "Sunday";
//			break;
//		case 2:
//			day = "Monday";
//			break;
//		case 3:
//			day = "Tuesday";
//			break;
//		case 4:
//			day = "Wednesday";
//			break;
//		case 5:
//			day = "Thursday";
//			break;
//		case 6:
//			day = "Friday";
//			break;
//		case 7:
//			day = "Saturday";
//			break;
//		}
//		return day;
//	}
//
//	
//	public static Date StringToDate(String dob) throws ParseException {
//		// Instantiating the SimpleDateFormat class
//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//		// Parsing the given String to Date object
//		Date date = formatter.parse(dob);
//		System.out.println("Date object value: " + date);
//		return date;
//
//	}
//	
	
	
}
