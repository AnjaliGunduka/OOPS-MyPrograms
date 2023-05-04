package com.mvc.Login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "redirect:login.jsp";
	}

	@RequestMapping(value = "/userCheck", method = RequestMethod.POST)
	public String userCheck(ModelMap model, HttpServletRequest request) {
		String name = request.getParameter("username");
		String pwd = request.getParameter("password");
		if ("Anjali".equalsIgnoreCase(name) && "12345".equalsIgnoreCase(pwd)) {
			model.addAttribute("message", "Successfully logged in.");

		} else {
			model.addAttribute("message", "Username or password is wrong.");
		}
		return "redirect:welcome.jsp";
	}
}
