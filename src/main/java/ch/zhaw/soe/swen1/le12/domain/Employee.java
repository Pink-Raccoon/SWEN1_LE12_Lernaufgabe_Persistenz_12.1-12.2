/* Copyright 2015 Oracle and/or its affiliates. All rights reserved. */

package ch.zhaw.soe.swen1.le12.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author luperalt, bacn
 */
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String lastName;
    private String birthDate;
    private String role;
    private String department;
    private String email;

    public Employee() {
        this.name = "";
        this.lastName = "";
        this.birthDate = "";
        this.role = "";
        this.department = "";
        this.email = "";
    }


    public Employee(String name, String lastName, String birthDate, String role, String department, String email) {
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.role = role;
        this.department = department;
        this.email = email;
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
        Date thisBirth = null;
        Date testBirth = null;
        try {
            thisBirth = format.parse(this.birthDate);
            testBirth = format.parse(testEmp.birthDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if ((this.id == testEmp.id) &&
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
}

