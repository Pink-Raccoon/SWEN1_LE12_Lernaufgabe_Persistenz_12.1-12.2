package com.example.employees.active.record;

import org.junit.Assert;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmployee {

    private Employee employee;
    private static long newId;
    private static int noOfRecords;

    @BeforeEach
    public void setUp() {
        this.employee = new Employee();
    }


    @Test
    @Order(1)
    public void shouldReturnSixRecords() {
        noOfRecords = employee.getAllEmployees().size();
        assertEquals(6, noOfRecords);
    }

    @Test
    @Order(2)
    public void shouldRetrieveNewEmployee() {
        int size = employee.getAllEmployees().size();
        Employee newEmp = new Employee("Simpson", "Williams", "1-01-1985", "Unemployed", "Finance", "simpson.williams@abc.com");
        newEmp.insert();
        newId = newEmp.getId();
        Employee retrievedEmployee = new Employee();
        retrievedEmployee.getEmployeeById(newId);
        Assert.assertEquals(retrievedEmployee, newEmp);
    }

    @Test
    @Order(3)
    public void shouldUpdateNewEmployee() {
        Employee newEmp = new Employee();
        newEmp.getEmployeeById(newId);
        newEmp.setName("Felix");
        newEmp.setLastName("Muster");
        newEmp.updateEmployee();
        Employee retrievedEmployee = new Employee();
        retrievedEmployee.getEmployeeById(newId);
        Assert.assertEquals(retrievedEmployee, newEmp);
    }

    @Test
    @Order(4)
    public void shouldDeleteEmployee() {
        Employee newEmp = new Employee();
        newEmp.getEmployeeById(newId);
        newEmp.removeEmployee();
        Assert.assertEquals(employee.getAllEmployees().size(), noOfRecords);
    }

    @Test
    @Order(5)
    public void shouldNotRetrieveDeletedEmployee() {
        Employee tryRetrieveDeleted = new Employee();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tryRetrieveDeleted.getEmployeeById(newId);
        });
    }
}
