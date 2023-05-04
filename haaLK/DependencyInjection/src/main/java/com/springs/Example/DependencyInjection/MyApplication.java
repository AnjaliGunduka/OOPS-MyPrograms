package com.springs.Example.DependencyInjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyApplication {
	// field-based dependency injection
	@Autowired
	private MessageService service;

	public MessageService getService() {
		return service;
	}

	public void setService(MessageService svc) {
		this.service = svc;
	}

	public boolean processMessage(String msg, String rec) {
		// some magic like validation, logging etc
		return this.service.sendMessage(msg, rec);
	}
}
