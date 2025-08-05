package businesslayers.observer;

import java.util.ArrayList;
import java.util.List;
import businesslayers.builder.Vehicle;

/**
 * Abstract subject for monitors in the Observer pattern.
 */
public abstract class MonitorSubject {

    private List<MonitorObserver> observers = new ArrayList<>();

    /**
     * Attaches an observer to this subject.
     *
     * @param observer the observer to attach
     */
    public void attach(MonitorObserver observer) {
        observers.add(observer);
    }

    /**
     * Detaches an observer from this subject.
     *
     * @param observer the observer to detach
     */
    public void detach(MonitorObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all attached observers of an issue.
     *
     * @param vehicle the affected vehicle
     * @param message the alert message
     */
    protected void notifyObservers(Vehicle vehicle, String message) {
        for (MonitorObserver observer : observers) {
            observer.update(vehicle, message);
        }
    }

    /**
     * Performs a system check and notifies observers if issues are found.
     */
    public abstract void check();
}
