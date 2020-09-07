package com.example.employees.jpa;


import com.example.employees.domain.Employee;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.logging.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmployeeJpaDao {

    private EmployeeJpaDao employeeJpaDao;
    private static final Logger LOGGER = Logger.getLogger(TestEmployeeJpaDao.class.getName());
    private static long newId;

    @BeforeEach
    public void setUp() {
        this.employeeJpaDao = new EmployeeJpaDao();
    }


    @Test
    @Order(1)
    public void shouldRetrieveNewEmployee()  throws SQLException {
        Employee employee = new Employee("Simpson", "Williams", "1-01-1985", "Unemployed", "Finance", "simpson.williams@abc.com");

        employeeJpaDao.insertEntity(employee);
        LOGGER.info("employee:: " + employee.toString());
        newId = employee.getId();
        Employee retrievedEmployee = employeeJpaDao.findEntityById(newId);
        Assert.assertEquals(retrievedEmployee, employee);
    }

    @Test
    @Order(2)
    public void shouldUpdateNewEmployee() {
        Employee employee = new Employee("Felix", "Muster", "1-01-1987", "Unemployed", "Sales", "felix.muster@abc.com");
        employee.setId(newId);
        employeeJpaDao.updateEntity(employee);
        Employee retrievedEmployee = employeeJpaDao.findEntityById(newId);
        Assert.assertEquals(retrievedEmployee, employee);

    }

    @Test
    @Order(3)
    public void shoulDeleteNewEmployee() {
        long beforeDelete = employeeJpaDao.count();
        employeeJpaDao.removeEntity(newId);
        long afterDelete = employeeJpaDao.count();
        Assert.assertEquals(beforeDelete, afterDelete + 1);

    }
}
