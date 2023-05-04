package com.student.library.services.entity;

import java.time.Instant;

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

import com.student.library.services.enums.RequestStatus;

@Entity
@Table(name = "REQUESTBOOK")
public class RequestBook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "BOOKNAME")
	private String bookName;
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private RequestStatus status;
	@Column(name = "STUDENT_CARDNO")
	private String cardNo;
	@Column(name = "NOOFBOOKS")
	private int noOfBooks;
	@Column(name = "REQUEST_DATE")
	private Instant requestDate;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STUDENT_ID", nullable = false)
	private Student student;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BOOK_ID", nullable = false)
	private Book book;

	public RequestBook(String cardNo, String bookName, RequestStatus status, int noOfBooks, Instant requestDate,
			Student student, Book book) {
		super();
		this.cardNo = cardNo;
		this.bookName = bookName;
		this.noOfBooks = noOfBooks;
		this.status = status;
		this.requestDate = requestDate;
		this.student = student;
		this.book = book;
	}

	

	public String getCardNo() {
		return cardNo;
	}



	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}



	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Instant getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Instant requestDate) {
		this.requestDate = requestDate;
	}

	public RequestBook() {
		super();
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

}
