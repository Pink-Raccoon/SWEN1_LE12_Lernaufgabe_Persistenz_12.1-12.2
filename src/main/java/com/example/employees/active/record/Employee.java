/* Copyright 2015 Oracle and/or its affiliates. All rights reserved. */

package com.example.employees.active.record;

import com.example.employees.config.DataBaseUtil;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luperalt, bacn
 */
public class Employee {

    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(Employee.class.getName());

    private long id;
    private String name;
    private String lastName;
    private String birthDate;
    private String role;
    private String department;
    private String email;

    public Employee() {
        this("", "", "", "", "", "");
    }

    public Employee(Employee employee) {
        this(employee.getName(), employee.getLastName(), employee.getBirthDate(), employee.getRole(), employee.getDepartment(),  employee.getEmail());
        this.id = employee.getId();
    }


    public Employee(String name, String lastName, String birthDate, String role, String department, String email) {
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.role = role;
        this.department = department;
        this.email = email;
        conn = DataBaseUtil.getConn();
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getEmail() {
        return this.email;
    }

    public long getId() {
        return this.id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object testObj) {
    	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    	Employee testEmp = (Employee) testObj;
    	Date thisBirth=null;
    	Date testBirth=null;
		try {
			thisBirth = format.parse(this.birthDate);
	    	testBirth = format.parse(testEmp.birthDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if ((this.id==testEmp.id) &&
    			this.name.equals(testEmp.name) &&
    			this.lastName.equals(testEmp.lastName) &&
    			this.role.equals(testEmp.role) &&
    			thisBirth.equals(testBirth) &&
    			this.department.equals(testEmp.department) &&
    			this.email.equals(testEmp.email))
    		return true;
		return false;

    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", name=" + this.name + ", lastName=" + this.lastName + ", birthDate="
                + this.birthDate + ", role=" + this.role + ", department=" + this.department + ", email=" + this.email
                + '}';
    }


    public long insert() {
        Employee employee = this;
        if (employee.getId() > 0) {
            throw new IllegalArgumentException("Id ist not 0");
        }
        long generatedId = -1;
        String statementString = "INSERT INTO PERSON(firstName,lastName,birthday,position,dept,email) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = conn.prepareStatement(statementString, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);

            int columnIndex = 0;
            statement.setString(++columnIndex, employee.getName());
            statement.setString(++columnIndex, employee.getLastName());
            Date date = resilientParsingDate(employee);
            if (null != date) {
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                statement.setDate(++columnIndex, sqlDate);
            } else {
                statement.setDate(++columnIndex, null);
            }
            statement.setString(++columnIndex, employee.getRole());
            statement.setString(++columnIndex, employee.getDepartment());
            statement.setString(++columnIndex, employee.getEmail());
            LOGGER.log(Level.INFO, "SQL Statement: {0}", statementString);
            statement.execute();

            generatedId = getGeneratedId(generatedId, statement);

            conn.commit();
            employee.id = generatedId;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error on add Employee:" + employee.getName(), e);
        }

        LOGGER.log(Level.INFO, "DB generated ID: {0}", generatedId);

        return generatedId;
    }

    private long getGeneratedId(long generatedID, PreparedStatement statement) {
        try (ResultSet resultSet = statement.getGeneratedKeys()) {
            while (resultSet.next()) {
                generatedID = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error on getting generated keys", e);
        }
        return generatedID;
    }

    private Date resilientParsingDate(Employee employee) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(employee.getBirthDate());
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Don't Panic :-) I'm trying to parse an empty String to DATE, anyway birthday is not required. So, you can ignore this message.");
        }
        return date;
    }


    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = loadAllPersonToEmployeeListFromH2DB();
        LOGGER.log(Level.INFO, "Get all Employee count: {0}", employeeList.size());
        return employeeList;
    }


    public Employee getEmployeeById(long id) {

        List<Employee> employeeList = loadOnePersonToEmployeeListFromH2DB(id);
        Optional<Employee> match
                = employeeList.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        if (match.isPresent()) {
            Employee employee = match.get();
            LOGGER.log(Level.INFO, "Get existing Employee:" + employee.getName());
            this.name = employee.getName();
            this.lastName = employee.getLastName();
            this.birthDate = employee.getBirthDate();
            this.role = employee.getRole();
            this.department = employee.getDepartment();
            this.email = employee.getEmail();
            this.id = employee.getId();
            return employee;
        } else {
            throw new IllegalArgumentException("The Employee id " + id + " not found");
        }
    }


    public boolean removeEmployee() {
        long id = this.id;
        boolean result = false;
        String deleteSQL = "DELETE FROM PERSON WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(deleteSQL)) {
            conn.setAutoCommit(false);
            statement.setLong(1, id);
            statement.execute();
            conn.commit();
            result = true;
            LOGGER.log(Level.INFO, "SQL deleted employee with id: {0}", id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error on delete Employee id:" + id, e);
        }

        return result;
    }


    public boolean updateEmployee() {
        Employee employee = this;
        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(employee.getBirthDate());
            String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String statementString = "UPDATE PERSON SET firstName = '" + employee.getName() + "', lastName = '" + employee.getLastName() + "', birthday = '" + stringDate + "', position = '" + employee.getRole() + "', dept = '" + employee.getDepartment() + "', email = '" + employee.getEmail() + "' WHERE id = " + employee.getId() + ";";
            LOGGER.log(Level.INFO, "SQL Statement: {0}", statementString);
            statement.execute(statementString);
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error on update Employee:" + employee.getName(), e);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing birthdate:" + employee.getBirthDate(), e);
        }

        return false;
    }

    private List<Employee> loadAllPersonToEmployeeListFromH2DB() {
        List<Employee> employeeList = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            employeeList = populateList(statement, "select * from PERSON");
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error on loadAllPersonToEmployeeListFromH2DB", e);
        }
        return employeeList;
    }

    private List<Employee> loadOnePersonToEmployeeListFromH2DB(long id) {
        List<Employee> employeeList = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            employeeList = populateList(statement, "select * from PERSON WHERE id=" + id);
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error on loadOnePersonToEmployeeListFromH2DB", e);
        }
        return employeeList;
    }


    private List<Employee> populateList(Statement statement, String query) {
        List<Employee> employeeList = new ArrayList<>();
        try (ResultSet statementResult = statement.executeQuery(query)) {
            while (statementResult.next()) {
                LOGGER.log(Level.INFO, "Id " + statementResult.getInt("id") + " Name " + statementResult.getString("firstName"));
                Employee emp = createEmployeeFromResultSet(statementResult);
                employeeList.add(emp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error on populating list", e);
        }
        return employeeList;
    }

    private Employee createEmployeeFromResultSet(ResultSet rs) throws SQLException {
       Employee employee = new Employee();
        employee.setName(rs.getString("firstName"));
        employee.setLastName(rs.getString("lastName"));
        Date date = rs.getDate("birthday");
        if (null != date) {
            employee.setBirthDate(new SimpleDateFormat("dd-MM-yyyy").format(date));
        }
        employee.setRole(rs.getString("position"));
        employee.setDepartment(rs.getString("dept"));
        employee.setEmail(rs.getString("email"));
        employee.setId(rs.getInt("id"));
        return employee;
    }


}

