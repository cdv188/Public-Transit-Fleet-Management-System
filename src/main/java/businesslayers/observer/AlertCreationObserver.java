package businesslayers.observer;

import businesslayers.builder.Vehicle;
import dataaccesslayer.maintenancelog.MaintenanceLogDAO;
import dataaccesslayer.maintenancelog.MaintenanceLogImpl;
import dataaccesslayer.maintenancelog.MaintenanceLogLogic;
import dataaccesslayer.maintenancelog.MaintenanceLog;
import java.sql.Date;

/**
 * Observer that creates alerts in the MaintenanceLog table
 * using Chester's final DAO implementation
 * @author Ali
 */
public class AlertCreationObserver implements MonitorObserver {
    
    private MaintenanceLogDAO maintenanceDAO;
    
    public AlertCreationObserver() {
        this.maintenanceDAO = new MaintenanceLogImpl();
    }
    
    @Override
    public void update(Vehicle vehicle, String message) {
        // Create a new maintenance log entry with Alert status
        MaintenanceLog alert = new MaintenanceLog();
        alert.setVehicleId(vehicle.getVehicleId());
        alert.setTaskDescription(message);
        alert.setStatus("Alert");
        alert.setScheduledDate(null);
        alert.setCompletionDate(null);
        
        // Save the alert using Chester's method
        boolean saved = maintenanceDAO.scheduleNewTask(alert);
        
        if (saved) {
            System.out.println("Alert created for vehicle " + vehicle.getNumber() + ": " + message);
        } else {
            System.err.println("Failed to create alert for vehicle " + vehicle.getNumber());
        }
    }
}
