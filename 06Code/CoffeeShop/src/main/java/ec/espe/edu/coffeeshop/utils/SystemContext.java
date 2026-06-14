package ec.espe.edu.coffeeshop.utils;

import ec.espe.edu.coffeeshop.controller.KitchenManager;
import ec.espe.edu.coffeeshop.model.Employee;

/**
 * Global application context (Singleton) to store session state
 * and share components like KitchenManager between frames.
 * 
 * @author MKA Programmers, ESPE
 */
public class SystemContext {
    private static SystemContext instance;
    
    private Employee currentUser;
    private final KitchenManager kitchenManager;

    private SystemContext() {
        this.kitchenManager = new KitchenManager();
    }

    public static synchronized SystemContext getInstance() {
        if (instance == null) {
            instance = new SystemContext();
        }
        return instance;
    }

    public Employee getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Employee currentUser) {
        this.currentUser = currentUser;
    }

    public KitchenManager getKitchenManager() {
        return kitchenManager;
    }
}
