package com.example.employees.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import com.example.employees.domain.Employee;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmployeeDaoImpl {

	private EmployeeDAOImpl employeeDAO;
	private static long testEmployeeId;

	@BeforeEach
	public void setUp() {
		this.employeeDAO = new EmployeeDAOImpl();
	}

	@Order(1)
	@Test
	public void shouldReturnSixRecords() throws SQLException {
		assertEquals(6, employeeDAO.getAllEmployees().size());
	}

	@Order(2)
	@Test
	public void shouldRetrieveNewEmployee()  throws SQLException {
		int size= employeeDAO.getAllEmployees().size();
		Employee testEmp = new Employee("Simpson", "Williams", "1-01-1985", "Unemployed", "Finance", "simpson.williams@abc.com");
		testEmployeeId = employeeDAO.addEmployee(testEmp);
		Assert.assertEquals(testEmployeeId, size + 1);
		testEmp.setId(testEmployeeId);
		Employee retrievedEmployee = employeeDAO.getEmployeeById(testEmployeeId);
		Assert.assertEquals(retrievedEmployee, testEmp);
	}


	@Order(5)
	@Test
	public void shouldDeleteEmployee() {
		employeeDAO.removeEmployee(testEmployeeId);
		Assert.assertEquals(6, employeeDAO.getAllEmployees().size());
	}
}
