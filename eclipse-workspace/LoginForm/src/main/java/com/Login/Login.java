package com.Login;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html");
		String user = req.getParameter("userName");
		String pass = req.getParameter("userPassword");
		pw.println("Login Success...!");

		if(user.equals("login") && pass.equals("login"))
			pw.println("Login Success...!");
		else
			pw.println("Login Failed...!");
		pw.close();
	}

}
