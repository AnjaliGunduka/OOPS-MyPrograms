package com.bookstore.service.prices.dto;


public class BookDto {

    private String title;
    private AuthorDto author;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public AuthorDto getAuthor() {
		return author;
	}
	public void setAuthor(AuthorDto author) {
		this.author = author;
	}

}
