package com.example.employees.jpa;

import com.example.employees.config.JPAUtil;
import com.example.employees.domain.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class EmployeeJpa {

    public void insertEntity(Employee employee) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Employee findEntityById(long id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        Employee employee = entityManager.find(Employee.class, id);
        entityManager.close();
        return employee;
    }

    public void updateEntity(Employee employee) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Employee retrievedEmployee = entityManager.find(Employee.class, employee.getId());
        retrievedEmployee.setName(employee.getName());
        retrievedEmployee.setLastName(employee.getLastName());
        retrievedEmployee.setRole(employee.getRole());
        retrievedEmployee.setEmail(employee.getEmail());
        retrievedEmployee.setBirthDate(employee.getBirthDate());
        retrievedEmployee.setDepartment(employee.getDepartment());

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void removeEntity(long id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Employee employee = entityManager.find(Employee.class, id);
        entityManager.remove(employee);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public long count () {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Query query=entityManager.createQuery("SELECT COUNT (emp.id) FROM Employee emp");
        long result =(long) query.getSingleResult();
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }


}
