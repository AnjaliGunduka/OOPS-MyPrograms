package com.SpringMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.SpringMvc.Service.AddService;

@Controller
public class AddController {
	@RequestMapping("/add")
	public ModelAndView add(@RequestParam("t1") int i, @RequestParam("t2") int j, HttpServletRequest request,
			HttpServletResponse response) {
//		System.out.println("hii im here");
//	int i=Integer.parseInt(request.getParameter("t1"));
//	int j=Integer.parseInt(request.getParameter("t2"));	
//	int k=i+j;
		AddService as = new AddService();
		int k = as.add(i, j);
		ModelAndView mv = new ModelAndView();// u can pass the data
		mv.setViewName("Display");
		mv.addObject("result", k);
		return mv;
	}
}
