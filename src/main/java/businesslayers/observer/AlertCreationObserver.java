package businesslayers.observer;

import businesslayers.builder.Vehicle;
import dataaccesslayer.maintenance.MaintenanceLogDAO;
import dataaccesslayer.maintenance.MaintenanceLogImpl;
import dataaccesslayer.maintenance.MaintenanceLog;

/**
 * Observer that creates maintenance log alerts for vehicles.
 */
public class AlertCreationObserver implements MonitorObserver {

    private MaintenanceLogDAO maintenanceDAO;

    /** Creates a new observer with a default DAO implementation. */
    public AlertCreationObserver() {
        this.maintenanceDAO = new MaintenanceLogImpl();
    }

    /**
     * Creates an alert in the maintenance log for the given vehicle.
     *
     * @param vehicle the vehicle triggering the alert
     * @param message the alert message
     */
    @Override
    public void update(Vehicle vehicle, String message) {
        MaintenanceLog alert = new MaintenanceLog();
        alert.setVehicleId(vehicle.getVehicleId());
        alert.setTaskDescription(message);
        alert.setStatus("Alert");
        alert.setScheduledDate(null);
        alert.setCompletionDate(null);

        boolean saved = maintenanceDAO.scheduleNewTask(alert);

        if (saved) {
            System.out.println("Alert created for vehicle " + vehicle.getNumber() + ": " + message);
        } else {
            System.err.println("Failed to create alert for vehicle " + vehicle.getNumber());
        }
    }
}
