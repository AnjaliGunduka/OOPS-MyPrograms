package com.example.adverts.model.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryQueryDto implements Serializable {

    private UUID id;
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long countSubCategories;

    public CategoryQueryDto(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

	public CategoryQueryDto(UUID id, String title, Long countSubCategories) {
		super();
		this.id = id;
		this.title = title;
		this.countSubCategories = countSubCategories;
	}

	public CategoryQueryDto() {
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

	public Long getCountSubCategories() {
		return countSubCategories;
	}

	public void setCountSubCategories(Long countSubCategories) {
		this.countSubCategories = countSubCategories;
	}

	

}
