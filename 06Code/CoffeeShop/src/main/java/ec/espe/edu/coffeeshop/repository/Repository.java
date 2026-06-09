package ec.espe.edu.coffeeshop.repository;

import java.util.List;

/**
 * Generic repository interface defining standard CRUD operations.
 * 
 * @author Anthony Aimacaña, MKA Programer, @ESPE
 * @param <T> The entity type.
 */
public interface Repository<T> {
    void save(T entity);
    T findById(String id);
    List<T> findAll();
    void delete(String id);
}
