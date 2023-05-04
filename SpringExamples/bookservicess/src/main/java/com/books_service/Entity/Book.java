package com.books_service.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String id = "";
    @Column(name = "TITLE")
    private String title = "";
    @Column(name = "PUBLISHDATE")
    private Date publishDate = null;
    @Column(name = "AUTHOR")
    private String author = "";
    @Column(name = "TYPE")
    private String type = "";
    @Column(name = "PRICE")
    private double price = 0;
    @Column(name = "NUMOFPAGES")
    private int numOfPages = 0;
    @Column(name = "PUBLISHER")
    private String publisher = "";
    @Column(name = "LANGUAGE")
    private String language = "";
    @Column(name = "ISBN13")
    private String isbn13 = "";


    public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setNumOfPages(int numOfPages) {
		this.numOfPages = numOfPages;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public Book() {
    }

    public Book(String id, String title, Date publishDate, String author, String type, double price, int numOfPages, String publisher, String language, String isbn13) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.author = author;
        this.type = type;
        this.price = price;
        this.numOfPages = numOfPages;
        this.publisher = publisher;
        this.language = language;
        this.isbn13 = isbn13;
    }

    public Book(String id2, String author2, String isbn132, String language2, int numOfPages2, double price2,
			Date publishDate2, String publisher2, String title2, String type2) {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getLanguage() {
        return language;
    }

    public String getIsbn13() {
        return isbn13;
    }

	

	

	
}
