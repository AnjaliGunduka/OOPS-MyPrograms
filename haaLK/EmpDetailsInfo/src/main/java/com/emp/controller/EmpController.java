package com.emp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.emp.bean.Emp;

@Controller  
public class EmpController {
	@RequestMapping("/empform")
	  public ModelAndView showform(){  
        //command is a reserved request attribute name, now use <form> tag to show object data  
       return new ModelAndView("empform","command",new Emp());  
   }  
   @RequestMapping(value="/save",method = RequestMethod.POST)  
   public ModelAndView save(@ModelAttribute("emp") Emp emp){  
       //write code to save emp object  
       //here, we are displaying emp object to prove emp has data  
       System.out.println(emp.getName()+" "+emp.getSalary()+" "+emp.getDesignation());  
         
       //return new ModelAndView("empform","command",emp);//will display object data  
       return new ModelAndView("redirect:/viewemp");//will redirect to viewemp request mapping  
   }  
     
   @RequestMapping("/viewemp")  
   public ModelAndView viewemp(){  
       //write the code to get all employees from DAO  
       //here, we are writing manual code of list for easy understanding  
       List<Emp> list=new ArrayList<Emp>();  
       list.add(new Emp(1,"Anjali",35000f,"S.Engineer"));  
       list.add(new Emp(2,"manisai",25000f,"IT Manager"));  
       list.add(new Emp(3,"SaiPavan",55000f,"Care Taker"));  
         
       return new ModelAndView("viewemp","list",list);  
   }
}
