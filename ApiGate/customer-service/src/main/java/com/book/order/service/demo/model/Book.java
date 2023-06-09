package com.book.order.service.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "bookslog")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id = "";
    private String title = "";
    private Date publishDate = null;
    private String author = "";
    private String type = "";
    private double price = 0;
    private int numOfPages = 0;
    private String publisher = "";
    private String language = "";
    private String isbn13 = "";


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
