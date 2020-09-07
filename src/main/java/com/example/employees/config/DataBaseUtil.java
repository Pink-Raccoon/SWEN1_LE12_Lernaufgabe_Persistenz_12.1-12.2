package com.example.employees.config;

import com.example.employees.dao.EmployeeDAOImpl;

import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseUtil {
    private static Connection conn=null;
    private static DataBaseUtil dataBaseUtil = null;
    private static final Logger LOGGER = Logger.getLogger(DataBaseUtil.class.getName());
    private static final String DBDRIVER = "org.h2.Driver";
    private static final String CONNECTIONCONFIG = "jdbc:h2:mem:test";

    public static Connection getConn() {
        if(dataBaseUtil == null) {
            dataBaseUtil = new DataBaseUtil();
            dataBaseUtil.init();
        }
        return conn;
    }

    public void init() {
        try{
            loadH2Driver();
            establishH2DBConnection();
            setUpH2Database();

        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "OMG!! You can't use the Database by now, sorry :-(", e);
        }
    }


    private void establishH2DBConnection() throws SQLException {
        try {
            conn = DriverManager.getConnection(CONNECTIONCONFIG, "sa", "");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection could not be established.", e);
            throw e;
        }
    }


    private void loadH2Driver() {
        try {
            Class.forName(DBDRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver "+DBDRIVER+" not found.", e);
        }
    }


    private void setUpH2Database() {

        try(Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            statement.execute("CREATE TABLE PERSON(id int primary key AUTO_INCREMENT, firstName varchar(255), lastName varchar(255), birthday DATE," +
                    "position varchar(100), dept varchar(100), email varchar(255));");
            String insertStatement ="INSERT INTO PERSON(firstName,lastName,birthday,position,dept,email) VALUES ";
            statement.execute(insertStatement +
                    "('John','Smith','1980-12-12','Manager','Sales','john.smith@abc.com');");
            statement.execute(insertStatement +
                    "('Laura','Adams','1979-11-02','Manager','IT','laura.adams@abc.com');");
            statement.execute(insertStatement +
                    "('Peter','Williams','1966-10-22','Coordinator','HR','peter.williams@abc.com');");
            statement.execute(insertStatement +
                    "('Joana','Sanders','1976-11-11','Manager','Marketing','joana.sanders@abc.com');");
            statement.execute(insertStatement +
                    "('John','Drake','1988-08-18','Coordinator','Finance','john.drake@abc.com');");
            statement.execute(insertStatement +
                    "('Samuel','Williams','1985-03-22','Coordinator','Finance','samuel.williams@abc.com');");

            conn.commit();
            ResultSet statementResult = statement.executeQuery("select * from PERSON");
            statementResult.last();
            int rows = statementResult.getRow();

            LOGGER.log(Level.INFO, "We have inserted a total of " + rows + " employees to the database " + conn.getMetaData().getURL());

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Don't Panic :-) If PERSON Table already exist, then no need to create it again. So, you can ignore this message.");
        }
    }

    private void dropPersonTable() {
        try(Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            statement.execute("DROP TABLE PERSON;");
            conn.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"Don't Panic :-) I'm trying to drop the PERSON table but it's already gone. So, you can ignore this message.");
        }
    }

    public void cleanUp() {
        dropPersonTable();
    }
}
