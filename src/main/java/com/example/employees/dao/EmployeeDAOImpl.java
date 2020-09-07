package com.example.employees.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.employees.config.DataBaseUtil;
import com.example.employees.domain.Employee;

public class EmployeeDAOImpl implements EmployeeDao {

    // private List<Employee> employeeList = null;
    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(EmployeeDAOImpl.class.getName());

    public EmployeeDAOImpl() {
        conn = DataBaseUtil.getConn();
    }

    @Override
    public long addEmployee(Employee employee) {
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

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = loadAllPersonToEmployeeListFromH2DB();
        LOGGER.log(Level.INFO, "Get all Employee count: {0}", employeeList.size());
        return employeeList;
    }

    @Override
    public Employee getEmployeeById(long id) {

        List<Employee> employeeList = loadOnePersonToEmployeeListFromH2DB(id);
        Optional<Employee> match
                = employeeList.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
        if (match.isPresent()) {
            Employee employee = match.get();
            LOGGER.log(Level.INFO, "Get existing Employee:" + employee.getName());
            return employee;
        } else {
            throw new IllegalArgumentException("The Employee id " + id + " not found");
        }
    }


    @Override
    public boolean removeEmployee(long id) {
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


    @Override
    public boolean updateEmployee(Employee employee) {
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
