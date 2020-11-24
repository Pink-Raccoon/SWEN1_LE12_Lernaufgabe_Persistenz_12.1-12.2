package ch.zhaw.soe.swen1.jpa;


import ch.zhaw.soe.swen1.domain.Employee;
import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.logging.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmployeeJpa {

    private EmployeeJpa employeeJpa;
    private static final Logger LOGGER = Logger.getLogger(TestEmployeeJpa.class.getName());
    private static long newId;

    @BeforeEach
    public void setUp() {
        this.employeeJpa = new EmployeeJpa();
    }


    @Test
    @Order(1)
    public void shouldRetrieveNewEmployee()  throws SQLException {
        Employee employee = new Employee("Simpson", "Williams", "1-01-1985", "Unemployed", "Finance", "simpson.williams@abc.com");

        employeeJpa.insertEntity(employee);
        LOGGER.info("employee:: " + employee.toString());
        newId = employee.getId();
        Employee retrievedEmployee = employeeJpa.findEntityById(newId);
        Assert.assertEquals(retrievedEmployee, employee);
    }


    @Test
    @Order(4)
    public void shoulDeleteNewEmployee() {
        long beforeDelete = employeeJpa.count();
        employeeJpa.removeEntity(newId);
        long afterDelete = employeeJpa.count();
        Assert.assertEquals(beforeDelete, afterDelete + 1);

    }
}
