package com.studentForm;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class StuRegister extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String first_name = request.getParameter("first_name");
		String last_name = request.getParameter("last_name");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String address = request.getParameter("address");
		String contact = request.getParameter("contact");

		if (first_name.isEmpty() || last_name.isEmpty() || username.isEmpty() || password.isEmpty() || address.isEmpty()
				|| contact.isEmpty()) {
			RequestDispatcher req = request.getRequestDispatcher("Register.jsp");
			req.include(request, response);
		} else {
			RequestDispatcher req = request.getRequestDispatcher("RegisterUser.jsp");
			req.forward(request, response);
		}
	}

}
