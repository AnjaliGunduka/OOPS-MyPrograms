package com.student.library.services.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.student.library.services.enums.RequestedStudent;

@Entity
@Table(name = "STUDENTBOOK")
public class StudentBookIssue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "REQUESTBOOK_STATUS")
	@Enumerated(EnumType.STRING)
	private RequestedStudent requestedstatus;
	@Column(name = "REQUESTBOOK_BOOKNAME")
	private String bookName;
	@Column(name = "RETURN_DATE")
	private Date returnDate;

	public RequestedStudent getRequestedstatus() {
		return requestedstatus;
	}

	public void setRequestedstatus(RequestedStudent requestedstatus) {
		this.requestedstatus = requestedstatus;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
