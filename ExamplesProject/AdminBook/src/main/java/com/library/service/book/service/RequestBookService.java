package com.library.service.book.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.library.service.book.entity.RequestBook;


import com.speedment.jpastreamer.application.JPAStreamer;




@Service
public class RequestBookService {

	@Autowired
	private JPAStreamer jpaStreamer;
	
	public List<RequestBook> getAllRequestedBooks() {
	return jpaStreamer.stream(RequestBook.class).collect(Collectors.toList());
	}
}
