package ch.zhaw.soe.swen1.le12.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ch.zhaw.soe.swen1.le12.dao.EmployeeDao;
import ch.zhaw.soe.swen1.le12.domain.Employee;

/**
 * Test cases and basic usage patterns for EmployeeDao based on JDBC. 
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmployeeDaoJdbc {

	private EmployeeDao employeeDAO;
	private static long testEmployeeId;

	@BeforeEach
	public void setUp() {
		this.employeeDAO = new EmployeeDaoJdbc();
	}

	@Order(1)
	@Test
	public void shouldReturnSixRecords() throws SQLException {
		assertEquals(6, employeeDAO.getAllEmployees().size());
		assertEquals(6, employeeDAO.count());
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
