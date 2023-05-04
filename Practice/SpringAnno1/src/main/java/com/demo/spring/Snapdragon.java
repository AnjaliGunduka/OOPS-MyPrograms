package com.demo.spring;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Snapdragon implements MobileProcessor {

	public void process() {
		System.out.println("World Best CPU");
		System.out.println("It is available in Nord 1");

	}
}
