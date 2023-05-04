package com.web.demo;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
//	@RequestMapping("home")
//	public ModelAndView home(@RequestParam("name") String myName);
//	
////			HttpSession session/* HttpServletRequest req, HttpServletResponse res */) {
////	 HttpSession session = req.getSession();
////	 String name = req.getParameter("name");
////		System.out.println("Hello " + myName);
////		session.setAttribute("name", myName);
//		
//	@RequestMapping("home")
//	public ModelAndView home(@RequestParam("name") String myName)
//	{
//	ModelAndView mv=new ModelAndView();
//	mv.addObject("name",myName);
//	mv.setViewName("home");
//	return mv;
//	}
	
	@RequestMapping("home")
	public ModelAndView home(Student student)
	{
	ModelAndView mv=new ModelAndView();
	mv.addObject("obj",student);
	mv.setViewName("home");
	return mv;
	}
	
}
