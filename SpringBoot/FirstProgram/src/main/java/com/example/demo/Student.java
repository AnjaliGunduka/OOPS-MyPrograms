package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class Student {
private int sid;
private String sname;
private String smarks;
@Autowired
@Qualifier("laptop")
private Laptop laptop;

public Student() {
	super();
	System.out.println("Object created");
}
public int getSid() {
	return sid;
}
public void setSid(int sid) {
	this.sid = sid;
}
public String getSname() {
	return sname;
}
public void setSname(String sname) {
	this.sname = sname;
}
public String getSmarks() {
	return smarks;
}
public void setSmarks(String smarks) {
	this.smarks = smarks;
}	
public Laptop getLaptop() {
	return laptop;
}
public void setLaptop(Laptop laptop) {
	this.laptop = laptop;
}
public void show()
{
	System.out.println("Im doing show");
	laptop.compile();
}

}
