package com.sergio.bookstore.service.prices.services;

import com.sergio.bookstore.service.prices.dto.PriceDto;
import com.sergio.bookstore.service.prices.exceptions.AppException;
import com.sergio.bookstore.service.prices.mappers.PriceMapper;
import com.sergio.bookstore.service.prices.respositories.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PriceService {

    private  PriceRepository priceRepository;
    private  PriceMapper priceMapper;
   // private  ServiceBooks serviceBooks;

    public PriceDto getPrice(long bookId) {
        var priceOptional = priceRepository.findByBookId(bookId);

        if (priceOptional.isEmpty()) {
            throw new AppException("No price for book " + bookId, HttpStatus.NOT_FOUND);
        }

        var priceDto = priceMapper.toPriceDto(priceOptional.get());

        var book = priceDto.getBook();
        priceDto.setBook(book);

        return priceDto;
    }
}
