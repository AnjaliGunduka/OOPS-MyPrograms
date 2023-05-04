package com.example.adverts.model.entity.product;

import com.example.adverts.model.entity.category.Category;
import com.example.adverts.model.entity.product_address.ProductAddress;
import com.example.adverts.model.entity.subcategory.SubCategory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "short_description")
    private String shortDescription;
    @Column(name = "price")
    private BigDecimal price;

//    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_address_id", referencedColumnName = "id", nullable = true)
    private ProductAddress productAddress;

//    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

//    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "sub_category_id", referencedColumnName = "id", nullable = false)
    private SubCategory subCategory;

	

	public Product() {
	super();
}

	public Product(UUID id, String title, String description, String shortDescription, BigDecimal price,
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
    
    
}
