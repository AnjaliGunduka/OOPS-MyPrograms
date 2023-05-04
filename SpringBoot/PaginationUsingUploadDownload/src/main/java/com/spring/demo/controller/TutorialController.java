package com.spring.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.demo.entity.Tutorials;
import com.spring.demo.repo.TutorialRepository;

@RestController
@RequestMapping("/api")
public class TutorialController {
	
	@Autowired
	private TutorialRepository tutorialRepository;

	/**
	 * getContent() to retrieve the List of items in the page. getNumber() for
	 * current Page. getTotalElements() for total items stored in database.
	 * getTotalPages() for number of total pages.
	 * 
	 * @param title
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/tutorials")
	public ResponseEntity<Map<String, Object>> getAllTutorials(@RequestParam(required = false) String title,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
		try {
			List<Tutorials> tutorials = new ArrayList<Tutorials>();
			Pageable paging = PageRequest.of(page, size);

			Page<Tutorials> pageTuts;
			if (title == null)
				pageTuts = tutorialRepository.findAll(paging);
			else
				pageTuts = tutorialRepository.findByTitleContaining(title, paging);
			tutorials = pageTuts.getContent();
			Map<String, Object> response = new HashMap<>();
			response.put("tutorials", tutorials);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * getContent() to retrieve the List of items in the page. getNumber() for
	 * current Page. getTotalElements() for total items stored in database.
	 * getTotalPages() for number of total pages.
	 * 
	 * @param title
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/tutorials/published")
	public ResponseEntity<Map<String, Object>> findByPublished(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		try {
			List<Tutorials> tutorials = new ArrayList<Tutorials>();
			Pageable paging = PageRequest.of(page, size);

			Page<Tutorials> pageTuts = tutorialRepository.findByPublished(true, paging);
			tutorials = pageTuts.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("tutorials", tutorials);
			response.put("currentPage", pageTuts.getNumber());
			response.put("totalItems", pageTuts.getTotalElements());
			response.put("totalPages", pageTuts.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping
	public ResponseEntity<Tutorials> addEmployee(@RequestBody Tutorials employee) {
		return new ResponseEntity<>(tutorialRepository.save(employee), HttpStatus.OK);
	}
	
	@PostMapping("/upload/db")
	public ResponseEntity uploadToDB(@RequestParam("file") MultipartFile file) {
		Tutorials doc = new Tutorials();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		doc.setDocName(fileName);
		try {
			doc.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		tutorialRepository.save(doc);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/files/download/")
				.path(fileName).path("/db")
				.toUriString();
		return ResponseEntity.ok(fileDownloadUri);
	}
	
	
	@GetMapping("/download/{fileName:.+}/db")
	public ResponseEntity downloadFromDB(@PathVariable String fileName) {
		Tutorials document = tutorialRepository.findByDocName(fileName);
		return ResponseEntity.ok()
				//.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(document.getFile());
	}
}
