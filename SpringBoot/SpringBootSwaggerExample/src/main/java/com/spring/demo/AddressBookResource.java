package com.spring.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api")
@Api
public class AddressBookResource {

	
	ConcurrentMap<String,Contact> contacts=new ConcurrentHashMap<>();
	@GetMapping("/{id}")
	@ApiOperation(value="Finds contacts by id",notes="provided an id to look up specific contact from the address book",response=Contact.class)
	public Contact getContact(@PathVariable String id)
	{
		return contacts.get(id);
	}
	@GetMapping("/")
	public List<Contact> getAllContacts()
	{
		return new ArrayList<Contact>(contacts.values());
	}
	@PostMapping("/")
	public Contact addContact(@RequestBody Contact contact)
	{
		contacts.put(contact.getId(), contact);
		return contact;
	}
}
