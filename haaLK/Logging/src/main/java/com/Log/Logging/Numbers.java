package com.Log.Logging;

import org.apache.log4j.Logger;

public class Numbers {

	public static Logger logg = Logger.getLogger(Numbers.class.getName());

	public static void main(String[] args) {
		int a = 8;
		int b = 6;
		System.out.println(add(a, b));
	}

	public static int add(int a, int b) {
		int c = a + b;
		return c;
	}


}
