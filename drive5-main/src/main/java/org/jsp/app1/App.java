package org.jsp.app1;

import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class App 
{
	User1 u;
	@RequestMapping("/sent")
	public  ModelAndView  sent(@RequestParam("ue") String ue1) {
	 ModelAndView m=null;
	 move(ue1);	
	 String s="deleted";
     m =new ModelAndView("display", "res", s);
     return m;
	}
	public static void move(String ue1) {
		User1 u=new User1();
		Configuration con=new Configuration().configure().addAnnotatedClass(User1.class);
		ServiceRegistry sr= new StandardServiceRegistryBuilder().applySettings(con.getProperties()).build();
      
    	SessionFactory sf=con.buildSessionFactory(sr);
       
    	Session s=sf.openSession();
        Transaction tx= s.beginTransaction();
    
        System.out.println(ue1);
         u= (User1)s.get(User1.class, ue1);
        s.delete(u);
        tx.commit();
        System.out.println("1   "+u);
	}
	


}
