package com.item.cart.model;

import lombok.Data;

import javax.persistence.*;

import com.item.cart.model.dto.ItemDto;

@Data
@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String serialNumber;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@ManyToOne
    private Cart cart;

    public static Item from(ItemDto itemDto){
        Item item = new Item();
        item.setSerialNumber(itemDto.getSerialNumber());
        return item;
    }
}
