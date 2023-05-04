package com.wavelabs.library.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wavelabs.library.model.Member;
import com.wavelabs.library.service.MemberService;

@RestController
@RequestMapping(value = "/rest/member")
public class MemberRestController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping(value = {"/", "/list"})
	public List<Member> all() {
		return memberService.getAll();
	}
	
}
