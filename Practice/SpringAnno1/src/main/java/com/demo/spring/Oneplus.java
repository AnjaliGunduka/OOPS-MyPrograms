package com.demo.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component  
public class Oneplus {
	@Autowired
	//@Qualifier("snapdragon")
	MobileProcessor cpu;

	public MobileProcessor getCpu() {
		return cpu;
	}

	public void setCpu(MobileProcessor cpu) {
		this.cpu = cpu;
	}

	public void config() 
	{
		System.out.println("12 + 256GB, Quad camera");
		cpu.process();
	}
}
