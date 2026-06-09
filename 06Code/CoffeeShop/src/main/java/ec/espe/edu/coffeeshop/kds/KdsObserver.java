package ec.espe.edu.coffeeshop.kds;

import ec.espe.edu.coffeeshop.model.Order;

/**
 * Observer interface for the Kitchen Display System.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public interface KdsObserver {
    void update(Order order);
}
