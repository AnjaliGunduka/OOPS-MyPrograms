package com.example.adverts.model.dto.product;

import com.example.adverts.model.entity.category.Category;
import com.example.adverts.model.entity.product_address.ProductAddress;
import com.example.adverts.model.entity.subcategory.SubCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductQueryDto implements Serializable {

    

	public ProductQueryDto() {
		super();
	}

	public ProductQueryDto(UUID id, String title, String description, String shortDescription, BigDecimal price,
			ProductAddress productAddress, Category category, SubCategory subCategory) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.shortDescription = shortDescription;
		this.price = price;
		this.productAddress = productAddress;
		this.category = category;
		this.subCategory = subCategory;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public ProductAddress getProductAddress() {
		return productAddress;
	}

	public void setProductAddress(ProductAddress productAddress) {
		this.productAddress = productAddress;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	private UUID id;
    private String title;
    private String description;
    private String shortDescription;
    private BigDecimal price;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductAddress productAddress;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "countSubCategories"})
    private Category category;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "category"})
    private SubCategory subCategory;

}
