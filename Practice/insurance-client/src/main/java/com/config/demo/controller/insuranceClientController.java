package com.config.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RefreshScope
public class insuranceClientController {
	
	@Autowired
	@Lazy
	private RestTemplate template;
	
	@Value("${insurance.provider.url}")
	private String url;
	
	@GetMapping("/getPlan")
	public List<String> getPlans(){
		List<String> plans=template.getForObject(url,List.class);
		return plans;
	}
}
