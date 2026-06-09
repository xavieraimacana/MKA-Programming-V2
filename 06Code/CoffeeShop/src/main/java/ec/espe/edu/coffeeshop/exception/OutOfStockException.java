package ec.espe.edu.coffeeshop.exception;

/**
 * Exception thrown when there is not enough stock of an ingredient.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}
