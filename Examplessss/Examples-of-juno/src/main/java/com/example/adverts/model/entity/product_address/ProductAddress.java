package com.example.adverts.model.entity.product_address;

import com.example.adverts.model.entity.product.Product;
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
@Table(name = "product_address")
public class ProductAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
    @Column(name = "address3")
    private String address3;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "county")
    private String county;
    @Column(name = "country")
    private String country;
    @Column(name = "zipcode")
    private String zipcode;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Product> products;

	

	public ProductAddress() {
		super();
	}

	public ProductAddress(UUID id, String address1, String address2, String address3, String city, String state,
			String county, String country, String zipcode, Collection<Product> products) {
		super();
		this.id = id;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.city = city;
		this.state = state;
		this.county = county;
		this.country = country;
		this.zipcode = zipcode;
		this.products = products;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

}
