package businesslayers.observer;

import businesslayers.builder.Vehicle;

/**
 * Observer interface for receiving monitoring alerts.
 */
public interface MonitorObserver {

    /**
     * Called when a monitor detects an issue with a vehicle.
     *
     * @param vehicle the affected vehicle
     * @param message the alert message
     */
    void update(Vehicle vehicle, String message);
}
