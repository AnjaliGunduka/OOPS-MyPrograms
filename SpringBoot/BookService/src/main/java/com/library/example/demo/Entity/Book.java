package com.library.example.demo.Entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "BOOK")
public class Book {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id ;
    @Column(name = "NAME")
    private String bookName;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "STATUS")
	private String status;
    @Column(name = "PUBLISHER")
    private String publisher;
    @Column(name="EDITION", length =20)
	private String edition;
    @Column(name="NUMOFCOPIES", length =5)
	private int numberOfCopies;
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;
    
    
	public Book(Long id, String bookName, String author, String status, String publisher, String edition,
			int numberOfCopies, Category category) {
		super();
		this.id = id;
		this.bookName = bookName;
		this.author = author;
		this.status = status;
		this.publisher = publisher;
		this.edition = edition;
		this.numberOfCopies = numberOfCopies;
		this.category = category;
	}
	
	public Book(int i, String string, String string2, String string3, String string4, String string5, int j,
			String string6, String string7) {
		// TODO Auto-generated constructor stub
	}

	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
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
}
