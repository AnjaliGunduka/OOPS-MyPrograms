package com.library.service.book.entity;

import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.library.service.book.enums.RequestedStudent;

@Entity
@Table(name = "STUDENTBOOK")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentBookIssue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "REQUESTBOOK_STATUS")
	@Enumerated(EnumType.STRING)
	private RequestedStudent requestedstatus;
	@Column(name = "ISSUE_DATE")
	private Date issueDate;
	@Column(name = "NOOFBOOKS")
	private int noOfBooks;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BOOK_ID", nullable = false)
	private Book book;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REQUESTBOOK_ID", nullable = false)
	private RequestBook requestBook;

	public RequestBook getRequestBook() {
		return requestBook;
	}

	public void setRequestBook(RequestBook requestBook) {
		this.requestBook = requestBook;
	}

	public StudentBookIssue() {
		super();
	}

	public StudentBookIssue(Date issueDate, int noOfBooks, RequestedStudent requestedstatus, Book book,
			RequestBook requestBook) {
		super();
		this.issueDate = issueDate;
		this.noOfBooks = noOfBooks;
		this.requestedstatus = requestedstatus;
		this.noOfBooks = noOfBooks;
		this.book = book;
		this.requestBook = requestBook;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RequestedStudent getRequestedstatus() {
		return requestedstatus;
	}

	public void setRequestedstatus(RequestedStudent requestedstatus) {
		this.requestedstatus = requestedstatus;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

}
