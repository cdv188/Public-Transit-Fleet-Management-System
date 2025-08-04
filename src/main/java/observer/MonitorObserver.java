/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

import vehicleSimpleFactory.Vehicle;

/**
 * Observer interface for monitoring system
 * @author Ali
 */
public interface MonitorObserver {
    /**
     * Method called when a monitor detects an issue
     * @param vehicle The vehicle with the issue
     * @param message Alert message describing the issue
     */
    void update(Vehicle vehicle, String message);
}