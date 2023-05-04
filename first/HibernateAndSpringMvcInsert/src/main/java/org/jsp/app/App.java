package org.jsp.app;

import java.util.ArrayList;

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
public class App {
	
	@RequestMapping("/sent")
	public static ModelAndView sent(@RequestParam("un") String un1,@RequestParam("ue") String ue1,
			
			@RequestParam("upw") String upw1,@RequestParam("upn") String upn1) {
		move(un1,ue1,upw1,upn1);
		ArrayList<String> a1=new ArrayList<String>();
		a1.add(un1);
		a1.add(ue1);
		a1.add(upw1);
		a1.add(upn1);
		ModelAndView m=new ModelAndView("display", "res", a1);
		return m;
		
		
		
		
	}
	public static void move(String un,String ue,String upw,String upn) {
		User1 user1=new User1();
    	user1.setUserEmail(ue);
    	user1.setUserName(un);
    	user1.setUserPassword(upw);
    	user1.setUserPn(upn);
		
		
		
		Configuration con=new Configuration().configure().addAnnotatedClass(User1.class);
//    	con.configure("/hibernate.cfg.xml");
    	ServiceRegistry sr= new StandardServiceRegistryBuilder().applySettings(con.getProperties()).build();
      
    	SessionFactory sf=con.buildSessionFactory(sr);
       
    	Session s=sf.openSession();
      Transaction tx= s.beginTransaction();
     s.save(user1);
     
     
      System.out.println("kkkk");
       tx.commit();
       s.close();
	}
	

}
