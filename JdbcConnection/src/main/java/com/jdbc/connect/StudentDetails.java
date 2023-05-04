package com.jdbc.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/StudentDetails")
public class StudentDetails extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		String n1 = req.getParameter("n1");
		// int n2=Integer.parseInt(req.getParameter("n2"));
		// res.getWriter().println("n1 " +n);
		String url = "jdbc:mysql://localhost:3306?user=root& password=Tiger";
		String query = "select * from employeeInforamtion.employe where id=?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// connection
			Connection c = DriverManager.getConnection(url);
			PreparedStatement s = c.prepareStatement(query);
			s.setString(1, n1);
			ResultSet r = s.executeQuery();
			if (r.next()) {
//				RequestDispatcher rd = req.getRequestDispatcher("error.jsp");
//				rd.include(req, res);
				res.getWriter().println(r.getString("first_name"));
				res.getWriter().println(r.getString("last_name"));
				res.getWriter().println(r.getString("address"));
				res.getWriter().println(r.getString("contact"));
			} else {
				res.getWriter().print(" not present");
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
