package com.springs.Example.DependencyInjection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DIConfiguration.class);
		MyApplication app = context.getBean(MyApplication.class);
		
		app.processMessage("Hi Anjali", "anjali@abc.com");
		//close the context
//		context.close();
	}
    }

