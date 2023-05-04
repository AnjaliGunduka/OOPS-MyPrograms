package com.wavelabs.action;

import com.opensymphony.xwork2.ActionSupport;

public class Product extends ActionSupport {
	private int id;
	private String name;
	private float price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String execute() {
		System.out.println("This is execute method");
		return "success";
	}
}