package com.example.adverts.model.dto.subcategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubCategoryCreateDto implements Serializable {

    private UUID id;
    private String title;
    private UUID categoryId;

    public SubCategoryCreateDto(String title, UUID categoryId) {
        this.title = title;
        this.categoryId = categoryId;
    }

	public SubCategoryCreateDto(UUID id, String title, UUID categoryId) {
		super();
		this.id = id;
		this.title = title;
		this.categoryId = categoryId;
	}

	public SubCategoryCreateDto() {
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

	public UUID getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(UUID categoryId) {
		this.categoryId = categoryId;
	}

	

}
