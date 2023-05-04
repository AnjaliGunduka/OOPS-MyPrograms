package com.codedecode.microservices.CitizenService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codedecode.microservices.CitizenService.Citizenservice;
import com.codedecode.microservices.CitizenService.Entity.Citizen;





@RestController
@RequestMapping("/citizen")
public class CitizenController {
	
	 @Autowired
	    private Citizenservice service;

    @PostMapping("/addCitizen")
    public Citizen addCitizen(@RequestBody Citizen citizen) {
        return service.saveCitizen(citizen);
    }

    @PostMapping("/addCitizens")
    public List<Citizen> addCitizens(@RequestBody List<Citizen> citizens) {
        return service.saveCitizens(citizens);
    }

    @GetMapping("/citizens")
    public List<Citizen> findAllCitizens() {
        return service.getCitizen();
    }

    @GetMapping("/citizenById/{id}")
    public Citizen findCitizenById(@PathVariable int id) {
        return service.getCitizenById(id);
    }

   

    @PutMapping("/update")
    public Citizen updateCitizen(@RequestBody Citizen citizen) {
        return service.saveCitizen(citizen);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCitizen(@PathVariable int id) {
        return service.deleteCitizen(id);
    }
	
}
