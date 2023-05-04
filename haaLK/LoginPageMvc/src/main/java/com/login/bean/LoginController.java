package com.login.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	    @RequestMapping("/userCheck")
	    public String userCheck(ModelMap model, HttpServletRequest request) {
	        String name=request.getParameter("name");
	        String pwd=request.getParameter("pwd");
	        if("manisai".equalsIgnoreCase(name)&&"12345".equalsIgnoreCase(pwd)){
	            model.addAttribute("message", "Successfully logged in.");
	            
	        }else{
	            model.addAttribute("message", "Username or password is wrong.");
	        }
	        return "welcome.jsp";
	    }
	    


	}


