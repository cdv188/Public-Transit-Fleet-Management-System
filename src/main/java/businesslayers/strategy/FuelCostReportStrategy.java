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
 * Strategy that generates a fuel cost analysis report.
 */
public class FuelCostReportStrategy implements ReportStrategy {

    private static final double DIESEL_COST_PER_LITER = 1.45;
    private static final double ELECTRICITY_COST_PER_KWH = 0.12;
    private static final double CNG_COST_PER_LITER = 0.95;

    /**
     * Generates a report containing fuel usage and cost data for all vehicles.
     */
    @Override
    public Map<String, Object> generate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> reportData = new HashMap<>();

        ConsumptionLogsDAO consumptionDAO = new ConsumptionLogsDAOImpl();
        VehicleLogic vehicleLogic = new VehicleLogic();

        List<Vehicle> vehicles = vehicleLogic.getAllVehicles();
        List<Map<String, Object>> vehicleCosts = new ArrayList<>();
        double totalCost = 0.0;

        for (Vehicle vehicle : vehicles) {
            List<ConsumptionLog> logs = consumptionDAO.getConsumptionByVehicle(vehicle.getVehicleId());

            double vehicleTotalUsage = logs.stream()
                                           .mapToDouble(ConsumptionLog::getUsageAmount)
                                           .sum();

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

        vehicleCosts.sort((a, b) -> Double.compare((Double) b.get("totalCost"), (Double) a.get("totalCost")));

        reportData.put("vehicleCosts", vehicleCosts);
        reportData.put("totalFleetCost", totalCost);
        reportData.put("reportDate", new Date());

        return reportData;
    }

    /** Returns the cost per unit for the given fuel type. */
    private double getCostPerUnit(String fuelType) {
        switch (fuelType.toLowerCase()) {
            case "diesel": return DIESEL_COST_PER_LITER;
            case "electricity": return ELECTRICITY_COST_PER_KWH;
            case "cng": return CNG_COST_PER_LITER;
            default: return DIESEL_COST_PER_LITER;
        }
    }

    /** @return the report title. */
    @Override
    public String getReportTitle() {
        return "Fuel Cost Analysis Report";
    }
}
