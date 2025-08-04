/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

import java.util.ArrayList;
import java.util.List;
import vehicleSimpleFactory.Vehicle;

/**
 * Abstract class for monitor subjects in the Observer pattern
 * @author Ali
 */
public abstract class MonitorSubject {
    private List<MonitorObserver> observers = new ArrayList<>();
    
    /**
     * Attach an observer to this subject
     * @param observer The observer to attach
     */
    public void attach(MonitorObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Detach an observer from this subject
     * @param observer The observer to detach
     */
    public void detach(MonitorObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notify all observers of a change
     * @param vehicle The vehicle affected
     * @param message The notification message
     */
    protected void notifyObservers(Vehicle vehicle, String message) {
        for (MonitorObserver observer : observers) {
            observer.update(vehicle, message);
        }
    }
    
    /**
     * Abstract method to check for issues
     * Implementations should check their specific conditions and notify observers if needed
     */
    public abstract void check();
}