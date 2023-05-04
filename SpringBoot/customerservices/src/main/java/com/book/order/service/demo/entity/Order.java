package com.book.order.service.demo.entity;


import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ORDER_DETAILS")
public class Order {

	
	@Id
	@Column(name = "BOOK_ID")
	private String bookId = "";

	@Column(name = "ISBN")
	private String isbn13 = "";

	@Column(name = "PUBLISH_DATE")
	private Date publishDate = null;
	@Column(name = "QUANITY")
	 private int quantity;
	public Order() {
		super();
	}
	public Order(String bookAuthor, String bookIsbn13, String bookLanguage, int bookNumOfPages,
			java.util.Date bookPublishDate, String bookPublisher, String bookTitle, String bookType, String price) {
		// TODO Auto-generated constructor stub
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getIsbn13() {
		return isbn13;
	}
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
