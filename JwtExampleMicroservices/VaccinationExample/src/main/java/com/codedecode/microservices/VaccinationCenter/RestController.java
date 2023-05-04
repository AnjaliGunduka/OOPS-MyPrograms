package com.codedecode.microservices.VaccinationCenter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.codedecode.microservices.VaccinationCenter.Model.RequiredResponse;



@FeignClient(url = "http://CITIZEN-SERVICE/citizen/id/", name ="REST-CONTROLLER")
public interface RestController {
	@GetMapping("/citizen")
	public List<RequiredResponse> getCitizens();
}
