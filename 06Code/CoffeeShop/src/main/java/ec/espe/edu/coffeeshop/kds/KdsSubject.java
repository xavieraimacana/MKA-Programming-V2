package ec.espe.edu.coffeeshop.kds;

/**
 * Subject interface for the Kitchen Display System (Observer Pattern).
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public interface KdsSubject {
    void addObserver(KdsObserver observer);
    void removeObserver(KdsObserver observer);
    void notifyObservers(ec.espe.edu.coffeeshop.model.Order order);
}
