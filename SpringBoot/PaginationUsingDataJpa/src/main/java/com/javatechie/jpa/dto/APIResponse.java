package com.javatechie.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {

    int recordCount;
    T response;
    
	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public APIResponse() {
		super();
	}

	public APIResponse(int recordCount, T response) {
		super();
		this.recordCount = recordCount;
		this.response = response;
	}

}
