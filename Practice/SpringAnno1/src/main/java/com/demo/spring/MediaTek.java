package com.demo.spring;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//@Primary
public class MediaTek implements MobileProcessor {

	public void process() {
		System.out.println("2nd Best CPU");
		System.out.println("It is available in Nord 2");

	}

}
