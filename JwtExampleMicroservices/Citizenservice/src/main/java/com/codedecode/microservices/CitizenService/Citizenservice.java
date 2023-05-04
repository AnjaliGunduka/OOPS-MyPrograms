package com.codedecode.microservices.CitizenService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.codedecode.microservices.CitizenService.Entity.Citizen;
import com.codedecode.microservices.CitizenService.repositories.CitizenRepo;





@Service
public class Citizenservice {
	@Autowired
	private CitizenRepo repository; 
	
	 public Citizen saveCitizen(Citizen citizen) {
	        return repository.save(citizen);
	    }
	 public List<Citizen> saveCitizens(List<Citizen> citizens) {
	        return repository.saveAll(citizens);
	    }

	    public List<Citizen> getCitizen() {
	        return repository.findAll();
	    }
	    
	    public Citizen getCitizenById(int id) {
	        return repository.findById(id).orElse(null);
	    }

	   
	
	
	 public String deleteCitizen(int id) {
	        repository.deleteById(id);
	        return "Citizen removed !! " + id;
	    }
	 
	 
	 
}
