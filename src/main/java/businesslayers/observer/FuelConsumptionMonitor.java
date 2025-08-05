package businesslayers.observer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DataSource;
import dataaccesslayer.vehicles.VehicleLogic;
import businesslayers.builder.Vehicle;
import java.util.HashSet;
import java.util.Set;

/**
 * Monitor that checks for vehicles with excessive fuel consumption.
 */
public class FuelConsumptionMonitor extends MonitorSubject {

    private static final double EXCESSIVE_THRESHOLD = 1.2; // 20% above expected

    // Check for ANY consumption above threshold, not just average
    private static final String CHECK_CONSUMPTION_QUERY = 
        "SELECT DISTINCT cl.vehicle_id, v.vehicle_number, v.consumption_rate, cl.usage_amount " +
        "FROM ConsumptionLogs cl " +
        "JOIN Vehicles v ON cl.vehicle_id = v.vehicle_id " +
        "WHERE cl.log_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
        "AND cl.usage_amount > (v.consumption_rate * ?)";

    @Override
    public void check() {
        System.out.println("FuelConsumptionMonitor: Starting check...");

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_CONSUMPTION_QUERY)) {

            pstmt.setDouble(1, EXCESSIVE_THRESHOLD);
            ResultSet rs = pstmt.executeQuery();

            VehicleLogic vehicleLogic = new VehicleLogic();
            Set<Integer> alertedVehicles = new HashSet<>(); // Avoid duplicate alerts

            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_id");

                if (!alertedVehicles.contains(vehicleId)) {
                    alertedVehicles.add(vehicleId);

                    String vehicleNumber = rs.getString("vehicle_number");
                    double expectedRate = rs.getDouble("consumption_rate");
                    double actualUsage = rs.getDouble("usage_amount");

                    Vehicle vehicle = vehicleLogic.getVehicleById(vehicleId);

                    if (vehicle != null) {
                        double percentageOver = ((actualUsage / expectedRate) - 1) * 100;
                        String message = String.format(
                            "ALERT: Vehicle %s showing excessive fuel consumption. " +
                            "Usage: %.2f (%.1f%% above expected rate of %.2f)",
                            vehicleNumber, actualUsage, percentageOver, expectedRate
                        );

                        System.out.println("Creating fuel alert: " + message);
                        notifyObservers(vehicle, message);
                    }
                }
            }

            System.out.println("FuelConsumptionMonitor: Checked " + alertedVehicles.size() + " vehicles with excessive consumption");

        } catch (SQLException e) {
            System.err.println("FuelConsumptionMonitor error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}