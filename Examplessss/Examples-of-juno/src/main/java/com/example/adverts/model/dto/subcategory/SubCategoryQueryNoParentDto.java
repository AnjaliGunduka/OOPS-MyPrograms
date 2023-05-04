package com.example.adverts.model.dto.subcategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubCategoryQueryNoParentDto implements Serializable {

   
	private UUID id;
    private String title;
	public SubCategoryQueryNoParentDto(UUID id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	public SubCategoryQueryNoParentDto() {
		super();
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

}
