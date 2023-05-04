package com.wavelabs.getapplication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import feign.Headers;

@Headers("Content-Type: application/json")
@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface RestClientService {
	@RequestMapping(value = "/app2/employee/save", method = RequestMethod.POST)
	Employee save(@RequestBody Employee employee);
}