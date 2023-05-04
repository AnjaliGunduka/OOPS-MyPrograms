package com.example.adverts.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FeedbackMessage {

    String message;

	

	public FeedbackMessage() {
		super();
	}

	public FeedbackMessage(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
}
