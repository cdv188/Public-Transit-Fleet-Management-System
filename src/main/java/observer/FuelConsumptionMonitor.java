/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package observer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import vehicelDAO.DataSource;
import vehicelDAO.VehicleLogic;
import vehicleSimpleFactory.Vehicle;

/**
 * Monitor for checking excessive fuel consumption
 * @author Ali
 */
public class FuelConsumptionMonitor extends MonitorSubject {
    
    private static final double EXCESSIVE_THRESHOLD = 1.2; // 20% above expected
    
    private static final String CHECK_CONSUMPTION_QUERY = 
        "SELECT cl.vehicle_id, v.vehicle_number, v.consumption_rate, " +
        "AVG(cl.usage_amount) as avg_usage " +
        "FROM ConsumptionLogs cl " +
        "JOIN Vehicles v ON cl.vehicle_id = v.vehicle_id " +
        "WHERE cl.log_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
        "GROUP BY cl.vehicle_id, v.vehicle_number, v.consumption_rate " +
        "HAVING avg_usage > (v.consumption_rate * ?)";
    
    @Override
    public void check() {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_CONSUMPTION_QUERY)) {
            
            pstmt.setDouble(1, EXCESSIVE_THRESHOLD);
            ResultSet rs = pstmt.executeQuery();
            
            VehicleLogic vehicleLogic = new VehicleLogic();
            
            while (rs.next()) {
                int vehicleId = rs.getInt("vehicle_id");
                String vehicleNumber = rs.getString("vehicle_number");
                double expectedRate = rs.getDouble("consumption_rate");
                double actualAverage = rs.getDouble("avg_usage");
                
                // Get the full vehicle object
                Vehicle vehicle = vehicleLogic.getVehicleById(vehicleId);
                
                if (vehicle != null) {
                    double percentageOver = ((actualAverage / expectedRate) - 1) * 100;
                    String message = String.format(
                        "ALERT: Vehicle %s showing excessive fuel consumption. " +
                        "Average: %.2f (%.1f%% above expected rate of %.2f)",
                        vehicleNumber, actualAverage, percentageOver, expectedRate
                    );
                    
                    // Notify all observers
                    notifyObservers(vehicle, message);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}