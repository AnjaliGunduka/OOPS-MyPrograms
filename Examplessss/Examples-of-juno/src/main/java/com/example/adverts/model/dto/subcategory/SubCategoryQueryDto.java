package com.example.adverts.model.dto.subcategory;

import com.example.adverts.model.entity.category.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubCategoryQueryDto implements Serializable {

    

	private UUID id;
    private String title;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

	public SubCategoryQueryDto(UUID id, String title, Category category) {
		super();
		this.id = id;
		this.title = title;
		this.category = category;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public SubCategoryQueryDto() {
		super();
	}

}
