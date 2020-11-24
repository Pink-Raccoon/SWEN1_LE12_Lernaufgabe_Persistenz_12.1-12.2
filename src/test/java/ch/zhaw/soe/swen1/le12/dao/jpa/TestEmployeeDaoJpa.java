package ch.zhaw.soe.swen1.le12.dao.jpa;


import ch.zhaw.soe.swen1.le12.domain.Employee;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.logging.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmployeeDaoJpa {

    private EmployeeDaoJpa employeeJpa;
    private static final Logger LOGGER = Logger.getLogger(TestEmployeeDaoJpa.class.getName());
    private static long newId;

    @BeforeEach
    public void setUp() {
        this.employeeJpa = new EmployeeDaoJpa();
    }


    @Test
    @Order(1)
    public void shouldRetrieveNewEmployee()  throws SQLException {
        Employee employee = new Employee("Simpson", "Williams", "1-01-1985", "Unemployed", "Finance", "simpson.williams@abc.com");

        newId = employeeJpa.addEmployee(employee);
        LOGGER.info("employee:: " + employee.toString());
        Employee retrievedEmployee = employeeJpa.getEmployeeById(newId);
        Assert.assertEquals(retrievedEmployee, employee);
    }


    @Test
    @Order(4)
    public void shoulDeleteNewEmployee() {
        long beforeDelete = employeeJpa.count();
        employeeJpa.removeEmployee(newId);
        long afterDelete = employeeJpa.count();
        Assert.assertEquals(beforeDelete, afterDelete + 1);

    }
}
