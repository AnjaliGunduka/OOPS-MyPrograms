package com.library.service.book.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.library.service.book.enums.Status;

@Entity
@Table(name = "BOOK")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "BOOKNAME")
	private String bookName;
	@Column(name = "AUTHOR")
	private String author;
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private Status status;
	@Column(name = "PUBLISHER")
	private String publisher;
	@Column(name = "EDITION")
	private String edition;
	@Column(name = "NUMOFCOPIES")
	private int numberOfCopies;
	@Column(name = "CATEGORY_NAME")
	private String catagoryName;

	public Book(Long id, String bookName, String author, Status status, String publisher, String edition,
			int numberOfCopies, String catagoryName) {
		super();
		this.id = id;
		this.bookName = bookName;
		this.author = author;
		this.status = status;
		this.publisher = publisher;
		this.edition = edition;
		this.numberOfCopies = numberOfCopies;
		this.catagoryName = catagoryName;
	}

	public Book() {
		super();
	}

	public String getCatagoryName() {
		return catagoryName;
	}

	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public int getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(int numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

}
