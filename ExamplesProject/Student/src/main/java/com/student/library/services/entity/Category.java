package com.student.library.services.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY")
public class Category {
	@Id
	@Column(name = "CATEGORY_NAME")
	private String catagoryName;
	@Column(name = "CATEGORY_LANG")
	private String catagoryLang;
	public String getCatagoryName() {
		return catagoryName;
	}
	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}
	public String getCatagoryLang() {
		return catagoryLang;
	}
	public void setCatagoryLang(String catagoryLang) {
		this.catagoryLang = catagoryLang;
	}

	
}
