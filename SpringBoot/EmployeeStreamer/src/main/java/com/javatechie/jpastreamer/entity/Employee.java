package com.javatechie.jpastreamer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.function.Predicate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMPLOYEE_RECORD")

public class Employee {
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String dept;

	public double getSalary() {
		return salary;
	}

	private double salary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Comparator<? super Employee> getName() {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
	
}
