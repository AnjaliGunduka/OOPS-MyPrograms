package com.jdbc.hiber.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jdbc.hiber.model.Employee;
import org.hibernate.SessionFactory;

@Repository
public class EmployeeDaoImpl implements EmployeeDAO {
@Autowired
	private SessionFactory sessionFactory;

	public void addEmployee(Employee employee) {
		sessionFactory.getCurrentSession().saveOrUpdate(employee);

	}

	@SuppressWarnings("unchecked")
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return sessionFactory.getCurrentSession().createQuery("from Employee").list();
	}

	public void deleteEmployee(Integer employeeId) {
		// TODO Auto-generated method stub
		Employee employee = (Employee) sessionFactory.getCurrentSession().load(Employee.class, employeeId);
		if (null != employee) {
			this.sessionFactory.getCurrentSession().delete(employee);
		}
	}

	public Employee updateEmployee(Employee employee) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().update(employee);
        return employee;
	}

	public Employee getEmployee(int empid) {
        return (Employee) sessionFactory.getCurrentSession().get(
                Employee.class, empid);
    }

}
