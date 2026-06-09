package ec.espe.edu.coffeeshop.controller;

import ec.espe.edu.coffeeshop.kds.KdsObserver;
import ec.espe.edu.coffeeshop.kds.KdsSubject;
import ec.espe.edu.coffeeshop.model.Order;
import ec.espe.edu.coffeeshop.model.OrderStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the kitchen order queue and notifies observers of status changes.
 * 
 * @author Kevin Albán, MKA Programmer, @ESPE
 */
public class KitchenManager implements KdsSubject {
    private final List<Order> preparationQueue = new ArrayList<>();
    private final List<KdsObserver> observers = new ArrayList<>();

    /**
     * Adds an order to the preparation queue.
     * 
     * @param order The order to be prepared.
     */
    public void receiveOrder(Order order) {
        order.setStatus(OrderStatus.PREPARING);
        preparationQueue.add(order);
        notifyObservers(order);
    }

    /**
     * Marks an order as ready and notifies observers.
     * 
     * @param orderId The ID of the order that is ready.
     */
    public void markOrderAsReady(String orderId) {
        for (Order order : preparationQueue) {
            if (order.getId().equals(orderId)) {
                order.setStatus(OrderStatus.READY);
                notifyObservers(order);
                // In a real flow, it might stay in queue until COMPLETED, 
                // but for KDS we notify it's READY.
                break;
            }
        }
    }

    @Override
    public void addObserver(KdsObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(KdsObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Order order) {
        for (KdsObserver observer : observers) {
            observer.update(order);
        }
    }

    public List<Order> getPreparationQueue() {
        return preparationQueue;
    }
}
