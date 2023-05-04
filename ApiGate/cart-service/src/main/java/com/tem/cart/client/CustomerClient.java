package com.tem.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@FeignClient(url = "${cartservices.domain}", name = "COLLEGE-CLIENT")
public interface CustomerClient {
//	@RequestMapping(value = "/cartServices/v1/exams/adding", method = RequestMethod.POST, consumes = "application/json")
//	public PlainCartDto adding(@RequestBody PlainCartDto examRequest);
}
