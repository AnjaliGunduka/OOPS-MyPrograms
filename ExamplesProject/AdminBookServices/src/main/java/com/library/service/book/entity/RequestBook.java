package com.library.service.book.entity;

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

import com.library.service.book.enums.RequestStatus;
import com.library.service.book.enums.RequestedStudent;


@Entity
@Table(name = "REQUESTBOOK")
public class RequestBook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "BOOKNAME")
	private String bookName;
	@Column(name = "STUDENT_CARDNO")
	private String cardNo;
	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private RequestStatus status;
	@Column(name = "NOOFBOOKS")
	private int noOfBooks;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STUDENT_ID", nullable = false)
	private Student student;

	public RequestBook(String cardNo, String bookName, RequestStatus status, int noOfBooks, Student student) {
		super();

		this.bookName = bookName;
		this.noOfBooks = noOfBooks;
		this.cardNo = cardNo;
		this.status = status;
		this.student = student;
	}

	public RequestBook() {
		super();
	}

	public String getCardNo() {
		return cardNo;
	}

	public int getNoOfBooks() {
		return noOfBooks;
	}

	public void setNoOfBooks(int noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	

	public void setStatus(RequestedStudent requestedstatus) {
		// TODO Auto-generated method stub
		
	}

}
