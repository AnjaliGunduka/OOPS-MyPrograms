package com.spring.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "tutorials")
public class Tutorials {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "title")
	private String title;
	@Column(name = "description")
	private String description;
	@Column(name = "published")
	private boolean published;
	@Column
	private String docName;
	@Column
	@Lob
	private byte[] file;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public Tutorials() {
		super();
	}

	

	public Tutorials(long id, String title, String description, boolean published, String docName, byte[] file) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.published = published;
		this.docName = docName;
		this.file = file;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
