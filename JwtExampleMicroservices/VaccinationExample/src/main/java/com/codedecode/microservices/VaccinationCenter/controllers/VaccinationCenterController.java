package com.codedecode.microservices.VaccinationCenter.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codedecode.microservices.VaccinationCenter.Entity.VaccinationCenter;
import com.codedecode.microservices.VaccinationCenter.Model.Citizen;
import com.codedecode.microservices.VaccinationCenter.Model.RequiredResponse;
import com.codedecode.microservices.VaccinationCenter.Repos.CenterRepo;



@RestController
@RequestMapping("/vaccinationcenter")
public class VaccinationCenterController {
	
	@Autowired
	private CenterRepo centerRepo;
	
	
	
	@PostMapping(path ="/add")
	public ResponseEntity<VaccinationCenter> addCitizen(@RequestBody VaccinationCenter vaccinationCenter) {
		
		VaccinationCenter vaccinationCenterAdded = centerRepo.save(vaccinationCenter);
		return new ResponseEntity<>(vaccinationCenterAdded, HttpStatus.OK);
	}
	
	@PostMapping(path = "/id/{id}")
	public ResponseEntity<RequiredResponse> getAllDadaBasedonCenterId(@PathVariable Integer id, @PathVariable  Integer vaccinationcenterId){
		RequiredResponse requiredResponse =  new RequiredResponse();
		//1st get vaccination center detail
		VaccinationCenter center  = centerRepo.findById(id).get();
		requiredResponse.setCenter(center);
		
		// then get all citizen registerd to vaccination center
		
		java.util.List<Citizen> listOfCitizens =requiredResponse .getCitizens();
		requiredResponse.setCitizens(listOfCitizens);
		return new ResponseEntity<RequiredResponse>(requiredResponse, HttpStatus.OK);
	}
	 @PostMapping(value = "{/CITIZEN-SERVICE/citizen/id}/id/{id}")
	    public ResponseEntity<RequiredResponse> getAllDadaBasedonCenterId1(@PathVariable  Integer citizenId,
	                                                 @PathVariable  Integer vaccinationcenterId){
		 VaccinationCenter center = centerRepo.findById(citizenId).get();
			RequiredResponse requiredResponse =  new RequiredResponse();
		 java.util.List<Citizen> listOfCitizens =requiredResponse.getCitizens();
	        return new ResponseEntity<RequiredResponse>(VaccinationCenter.from((List<Citizen>) center), HttpStatus.OK);
	    }
	
	
	
	

}
