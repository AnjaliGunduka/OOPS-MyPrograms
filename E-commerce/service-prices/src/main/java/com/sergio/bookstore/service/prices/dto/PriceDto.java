package com.sergio.bookstore.service.prices.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PriceDto {

    private BigDecimal price;
    private BookDto book;
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BookDto getBook() {
		return book;
	}
	public void setBook(BookDto book) {
		this.book = book;
	}
}
