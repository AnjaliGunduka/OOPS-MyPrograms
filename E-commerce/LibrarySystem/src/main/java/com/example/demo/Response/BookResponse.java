package com.example.demo.Response;

public class BookResponse {
	private String bookName;
	private String author;
	private String status;
	private String publisher;
	private String edition;
	private int numberOfCopies;
	private String CatagoryName;
	private String CatagoryLang;
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
