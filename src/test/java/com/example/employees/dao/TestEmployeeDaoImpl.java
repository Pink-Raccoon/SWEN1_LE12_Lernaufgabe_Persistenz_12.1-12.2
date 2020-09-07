package com.example.employees.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.example.employees.domain.Employee;

public class TestEmployeeDaoImpl {

	private EmployeeDAOImpl employeeDAO;

	@BeforeEach
	public void setUp() {
		this.employeeDAO = new EmployeeDAOImpl();
	}


	@Test
	public void shouldReturnSixRecords() throws SQLException {
		assertEquals(6, employeeDAO.getAllEmployees().size());
	}

	@Test
	public void shouldRetrieveNewEmployee()  throws SQLException {
		int size= employeeDAO.getAllEmployees().size();
		Employee testEmp = new Employee("Simpson", "Williams", "1-01-1985", "Unemployed", "Finance", "simpson.williams@abc.com");
		long testEmployeeId = employeeDAO.addEmployee(testEmp);
		Assert.assertEquals(testEmployeeId, size + 1);
		testEmp.setId(testEmployeeId);
		Employee retrievedEmployee = employeeDAO.getEmployeeById(testEmployeeId);
		Assert.assertEquals(retrievedEmployee, testEmp);
	}


}
