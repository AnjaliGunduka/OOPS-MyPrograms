package com.book.order.service.demo.mapper;



import org.springframework.stereotype.Service;

import com.book.order.service.demo.entity.Order;
import com.book.order.service.demo.response.BookResponse;


@Service
public class OrderMapper {

	public Order mapBookResponseToOrder(BookResponse bookResponse, String Price) {
		return new Order(bookResponse.getBookAuthor(), bookResponse.getBookIsbn13(), bookResponse.getBookLanguage(),
				bookResponse.getBookNumOfPages(), bookResponse.getBookPublishDate(), bookResponse.getBookPublisher(),
				bookResponse.getBookTitle(), bookResponse.getBookType(), Price);
	}
}
