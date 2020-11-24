package ch.zhaw.soe.swen1.le12.dao;

import java.util.List;

import ch.zhaw.soe.swen1.le12.domain.Employee;

public interface EmployeeDao {

    long addEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(long id);

    boolean removeEmployee(long id);

    boolean updateEmployee(Employee employee);

}
