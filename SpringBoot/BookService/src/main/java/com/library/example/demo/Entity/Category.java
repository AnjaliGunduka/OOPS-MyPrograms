package com.library.example.demo.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY")
public class Category {
	@Id
	@Column(name = "CATEGORY_NAME")
	private String CatagoryName;
	@Column(name = "CATEGORY_LANG")
	private String CatagoryLang;

	public String getCatagoryName() {
		return CatagoryName;
	}

	public void setCatagoryName(String catagoryName) {
		CatagoryName = catagoryName;
	}

	public String getCatagoryLang() {
		return CatagoryLang;
	}

	public void setCatagoryLang(String catagoryLang) {
		CatagoryLang = catagoryLang;
	}
}
