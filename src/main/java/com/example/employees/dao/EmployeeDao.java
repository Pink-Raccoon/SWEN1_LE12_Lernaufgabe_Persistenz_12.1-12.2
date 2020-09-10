package com.example.employees.dao;

import java.util.List;

import com.example.employees.domain.Employee;

public interface EmployeeDao {

    long addEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(long id);

    boolean removeEmployee(long id);

    boolean updateEmployee(Employee employee);

}
