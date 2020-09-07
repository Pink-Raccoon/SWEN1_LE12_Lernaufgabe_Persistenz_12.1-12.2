package com.example.employees.dao;

import java.util.List;

import com.example.employees.domain.Employee;

public interface EmployeeDao {

    public long addEmployee(Employee employee);

    public List<Employee> getAllEmployees();

    public Employee getEmployeeById(long id);

    public boolean removeEmployee(long id);

    public boolean updateEmployee(Employee employee);
}
