package com.library.service.book.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY")
public class Category {
	@Id
	@Column(name = "CATEGORY_SUB")
	private String catagorySub;
	@Column(name = "CATEGORY_NAME")
	private String catagoryName;

	public String getCatagoryName() {
		return catagoryName;
	}

	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}

	public String getCatagorySub() {
		return catagorySub;
	}

	public void setCatagorySub(String catagorySub) {
		this.catagorySub = catagorySub;
	}

}
