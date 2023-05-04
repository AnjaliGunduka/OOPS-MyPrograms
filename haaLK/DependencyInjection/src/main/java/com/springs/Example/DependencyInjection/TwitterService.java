package com.springs.Example.DependencyInjection;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
@Component

public class TwitterService implements MessageService {

	public boolean sendMessage(String msg, String rec) {
		// TODO Auto-generated method stub
		System.out.println("Twitter message Sent to " + rec + " with Message=" + msg);
		return true;

	}

}
