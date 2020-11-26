package ch.zhaw.soe.swen1.le12.dao;

import java.util.List;

import ch.zhaw.soe.swen1.le12.domain.Employee;

/**
 * DAO interface for employee objects. 
 */
public interface EmployeeDao {

    long addEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(long id);

    void removeEmployee(long id);

    void updateEmployee(Employee employee);

    long count();

}
