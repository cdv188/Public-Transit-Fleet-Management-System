/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package businesslayers.strategy;

import dataaccesslayer.consumptionlogs.ConsumptionLogsDAO;
import dataaccesslayer.consumptionlogs.ConsumptionLogsDAOImpl;
import datatransferobject.ConsumptionLog;
import dataaccesslayer.vehicles.VehicleLogic;
import businesslayers.builder.Vehicle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Strategy for generating fuel cost reports
 * @author Ali
 */
public class FuelCostReportStrategy implements ReportStrategy {
    
    private static final double DIESEL_COST_PER_LITER = 1.45;
    private static final double ELECTRICITY_COST_PER_KWH = 0.12;
    private static final double CNG_COST_PER_LITER = 0.95;
    
    @Override
    public Map<String, Object> generate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> reportData = new HashMap<>();
        
        ConsumptionLogsDAO consumptionDAO = new ConsumptionLogsDAOImpl();
        VehicleLogic vehicleLogic = new VehicleLogic();
        
        // Get all vehicles
        List<Vehicle> vehicles = vehicleLogic.getAllVehicles();
        
        List<Map<String, Object>> vehicleCosts = new ArrayList<>();
        double totalCost = 0.0;
        
        for (Vehicle vehicle : vehicles) {
            List<ConsumptionLog> logs = consumptionDAO.getConsumptionByVehicle(vehicle.getVehicleId());
            
            double vehicleTotalUsage = 0.0;
            for (ConsumptionLog log : logs) {
                vehicleTotalUsage += log.getUsageAmount();
            }
            
            // Calculate cost based on fuel type
            double costPerUnit = getCostPerUnit(vehicle.getFuelType());
            double vehicleCost = vehicleTotalUsage * costPerUnit;
            totalCost += vehicleCost;
            
            Map<String, Object> vehicleData = new HashMap<>();
            vehicleData.put("vehicleNumber", vehicle.getNumber());
            vehicleData.put("vehicleType", vehicle.getVehicleType());
            vehicleData.put("fuelType", vehicle.getFuelType());
            vehicleData.put("totalUsage", vehicleTotalUsage);
            vehicleData.put("costPerUnit", costPerUnit);
            vehicleData.put("totalCost", vehicleCost);
            vehicleData.put("logCount", logs.size());
            
            vehicleCosts.add(vehicleData);
        }
        
        // Sort by cost (highest first)
        vehicleCosts.sort((a, b) -> Double.compare((Double)b.get("totalCost"), (Double)a.get("totalCost")));
        
        reportData.put("vehicleCosts", vehicleCosts);
        reportData.put("totalFleetCost", totalCost);
        reportData.put("reportDate", new Date());
        
        return reportData;
    }
    
    private double getCostPerUnit(String fuelType) {
        switch (fuelType.toLowerCase()) {
            case "diesel":
                return DIESEL_COST_PER_LITER;
            case "electricity":
                return ELECTRICITY_COST_PER_KWH;
            case "cng":
                return CNG_COST_PER_LITER;
            default:
                return DIESEL_COST_PER_LITER; // Default to diesel
        }
    }
    
    @Override
    public String getReportTitle() {
        return "Fuel Cost Analysis Report";
    }
}