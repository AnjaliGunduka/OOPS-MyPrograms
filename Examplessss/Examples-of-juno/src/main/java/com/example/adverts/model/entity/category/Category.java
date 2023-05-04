package com.example.adverts.model.entity.category;

import com.example.adverts.model.entity.product.Product;
import com.example.adverts.model.entity.subcategory.SubCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "category")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "title")
    private String title;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<SubCategory> subCategories;

//    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Product> products;

	

	public Category() {
		super();
	}

	public Category(UUID id, String title, Collection<SubCategory> subCategories, Collection<Product> products) {
	super();
	this.id = id;
	this.title = title;
	this.subCategories = subCategories;
	this.products = products;
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

	public Collection<SubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Collection<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

}
