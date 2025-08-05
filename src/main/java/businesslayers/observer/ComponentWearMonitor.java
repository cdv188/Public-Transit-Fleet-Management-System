package businesslayers.observer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DataSource;
import dataaccesslayer.vehicles.VehicleLogic;
import businesslayers.builder.Vehicle;

/**
 * Monitor that checks for vehicle components approaching wear thresholds.
 */
public class ComponentWearMonitor extends MonitorSubject {

    private static final String CHECK_WEAR_QUERY =
        "SELECT vc.*, v.vehicle_number " +
        "FROM VehicleComponents vc " +
        "JOIN Vehicles v ON vc.vehicle_id = v.vehicle_id " +
        "WHERE vc.hours_of_use >= (vc.wear_threshold_hours * 0.9)";

    /**
     * Checks all vehicle components for wear levels and notifies observers if near threshold.
     */
    @Override
    public void check() {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_WEAR_QUERY);
             ResultSet rs = pstmt.executeQuery()) {

            VehicleLogic vehicleLogic = new VehicleLogic();

            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_id");
                String componentName = rs.getString("component_name");
                int hoursOfUse = rs.getInt("hours_of_use");
                int threshold = rs.getInt("wear_threshold_hours");
                String vehicleNumber = rs.getString("vehicle_number");

                Vehicle vehicle = vehicleLogic.getVehicleById(vehicleId);

                if (vehicle != null) {
                    double percentageWorn = (double) hoursOfUse / threshold * 100;
                    String message = String.format(
                        "ALERT: %s on vehicle %s has reached %.1f%% of wear threshold (%d/%d hours)",
                        componentName, vehicleNumber, percentageWorn, hoursOfUse, threshold
                    );
                    notifyObservers(vehicle, message);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
