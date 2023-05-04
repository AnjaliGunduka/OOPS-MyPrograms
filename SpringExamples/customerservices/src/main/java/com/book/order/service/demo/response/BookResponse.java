package com.book.order.service.demo.response;

import java.util.Date;

public class BookResponse {
	private String bookId = "";
    private String bookTitle = "";
    private Date bookPublishDate = null;
    private String bookAuthor = "";
    private String bookType = "";
    private double Price = 0;
    public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	private int bookNumOfPages = 0;
    private String bookPublisher = "";
    private String bookLanguage = "";
    private String bookIsbn13 = "";
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public Date getBookPublishDate() {
		return bookPublishDate;
	}
	public void setBookPublishDate(Date bookPublishDate) {
		this.bookPublishDate = bookPublishDate;
	}
	public String getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	public String getBookType() {
		return bookType;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
	
	public int getBookNumOfPages() {
		return bookNumOfPages;
	}
	public void setBookNumOfPages(int bookNumOfPages) {
		this.bookNumOfPages = bookNumOfPages;
	}
	public String getBookPublisher() {
		return bookPublisher;
	}
	public void setBookPublisher(String bookPublisher) {
		this.bookPublisher = bookPublisher;
	}
	public String getBookLanguage() {
		return bookLanguage;
	}
	public void setBookLanguage(String bookLanguage) {
		this.bookLanguage = bookLanguage;
	}
	public String getBookIsbn13() {
		return bookIsbn13;
	}
	public void setBookIsbn13(String bookIsbn13) {
		this.bookIsbn13 = bookIsbn13;
	}
	
}
