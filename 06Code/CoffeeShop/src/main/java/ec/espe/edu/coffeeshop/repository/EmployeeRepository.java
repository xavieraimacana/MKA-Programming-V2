package ec.espe.edu.coffeeshop.repository;

import ec.espe.edu.coffeeshop.model.Employee;

/**
 * Repository interface for Employee entities.
 * Includes credentials search for authentication.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 */
public interface EmployeeRepository extends Repository<Employee> {
    /**
     * Finds an employee matching the given username and password.
     */
    Employee findByCredentials(String username, String password);
}
