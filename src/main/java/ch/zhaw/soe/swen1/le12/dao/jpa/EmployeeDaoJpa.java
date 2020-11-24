package ch.zhaw.soe.swen1.le12.dao.jpa;

import ch.zhaw.soe.swen1.le12.config.JPAUtil;
import ch.zhaw.soe.swen1.le12.dao.EmployeeDao;
import ch.zhaw.soe.swen1.le12.domain.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class EmployeeDaoJpa implements EmployeeDao {

    @Override
    public long addEmployee(Employee employee) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
        entityManager.close();
        return employee.getId();
    }

    @Override
    public Employee getEmployeeById(long id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        Employee employee = entityManager.find(Employee.class, id);
        entityManager.close();
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        return entityManager.createQuery("from" + Employee.class.getName(), Employee.class)
                .getResultList();
    }

    @Override
    public void updateEmployee(Employee employee) {
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

    @Override
    public void removeEmployee(long id) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Employee employee = entityManager.find(Employee.class, id);
        entityManager.remove(employee);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public long count() {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("SELECT COUNT (emp.id) FROM Employee emp");
        long result = (long) query.getSingleResult();
        entityManager.getTransaction().commit();
        entityManager.close();
        return result;
    }


}
