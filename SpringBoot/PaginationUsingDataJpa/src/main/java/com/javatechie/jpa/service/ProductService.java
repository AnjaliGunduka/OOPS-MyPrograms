package com.javatechie.jpa.service;

import com.javatechie.jpa.entity.Product;
import com.javatechie.jpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

//	@PostConstruct
//	public void initDB() {
//		List<Product> products = IntStream.rangeClosed(1, 200)
//				.mapToObj(i -> new Product("product" + i, new Random().nextInt(20), new Random().nextInt(50)))
//				.collect(Collectors.toList());
//		repository.saveAll(products);
//	}

	public List<Product> findAllProducts() {
		return repository.findAll();
	}

	/**
	 * Field you can search any element(ie,.price,id,..) based on the Sorting As an
	 * Ascending order
	 * 
	 * @param field
	 * @return
	 */

	public List<Product> findProductsWithSorting(String field) {
		return repository.findAll(Sort.by(Sort.Direction.ASC, field));
	}

	/**
	 * Here we want to search Pages Based on number of elements Offset is a next
	 * element PageSize is per page no of records we want
	 * 
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public Page<Product> findProductsWithPagination(int offset, int pageSize) {
		Page<Product> products = repository.findAll(PageRequest.of(offset, pageSize));
		return products;
	}

	/**
	 * Here we want to search Pages Based on number of elements Offset is a next
	 * element PageSize is per page no of records we want offset zero-based page
	 * index, must not be negative.
	 * 
	 * @param pageSize the size of the page to be returned, must be greater than 0.
	 *                 Field you can search any element(ie,.price,id,..) based on
	 *                 the Sorting
	 * @param offset
	 * @param pageSize
	 * @param field
	 * @return
	 */
	public Page<Product> findProductsWithPaginationAndSorting(int offset, int pageSize, String field) {
		Page<Product> products = repository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
		return products;
	}

	public Page<Product> employeesPageable(Pageable pageable) {
		return repository.findAll(pageable);

	}
}
