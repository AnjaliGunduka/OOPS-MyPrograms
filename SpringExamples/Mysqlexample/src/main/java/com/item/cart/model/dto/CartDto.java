package com.item.cart.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.item.cart.model.Cart;
import com.item.cart.model.Item;

@Data
public class CartDto {
    private Long id;
    private String name;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ItemDto> getItemsDto() {
		return itemsDto;
	}

	public void setItemsDto(List<ItemDto> itemsDto) {
		this.itemsDto = itemsDto;
	}

	private List<ItemDto> itemsDto = new ArrayList<>();

    public static CartDto from(Cart cart){
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setName(cart.getName());
        cartDto.setItemsDto(cart.getItems().stream().map(ItemDto::from).collect(Collectors.toList()));
        return cartDto;
    }
}
