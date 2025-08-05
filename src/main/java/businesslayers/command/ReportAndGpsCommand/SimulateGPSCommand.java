package businesslayers.command.ReportAndGpsCommand;

import dataaccesslayer.vehiclelocation.VehicleLocationsDAO;
import dataaccesslayer.vehiclelocation.VehicleLocationsDAOImpl;
import dataaccesslayer.consumptionlogs.ConsumptionLogsDAO;
import dataaccesslayer.consumptionlogs.ConsumptionLogsDAOImpl;
import datatransferobject.VehicleLocation;
import datatransferobject.ConsumptionLog;
import dataaccesslayer.vehicles.VehicleLogic;
import businesslayers.builder.Vehicle;
import businesslayers.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.Random;

/**
 * Command that simulates GPS movement for vehicles and logs fuel consumption.
 */
public class SimulateGPSCommand implements Command {

    private static final double OTTAWA_LAT_MIN = 45.25;
    private static final double OTTAWA_LAT_MAX = 45.50;
    private static final double OTTAWA_LON_MIN = -76.00;
    private static final double OTTAWA_LON_MAX = -75.50;

    /**
     * Executes GPS simulation for all vehicles.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        executeWithResult(request, response);
    }

    /**
     * Runs the GPS simulation and returns an optional result string.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return an optional result string, or {@code null} if handled directly
     */
    public String executeWithResult(HttpServletRequest request, HttpServletResponse response) {
        try {
            VehicleLogic vehicleLogic = new VehicleLogic();
            VehicleLocationsDAO locationsDAO = new VehicleLocationsDAOImpl();
            ConsumptionLogsDAO consumptionDAO = new ConsumptionLogsDAOImpl();

            List<Vehicle> vehicles = vehicleLogic.getAllVehicles();
            Random random = new Random();
            int updatedCount = 0;
            int consumptionLogsCreated = 0;

            for (Vehicle vehicle : vehicles) {
                VehicleLocation currentLocation = locationsDAO.getCurrentLocation(vehicle.getVehicleId());
                double newLat, newLon;

                if (currentLocation != null) {
                    double oldLat = currentLocation.getLatitude();
                    double oldLon = currentLocation.getLongitude();

                    newLat = currentLocation.getLatitude() + (random.nextDouble() - 0.5) * 0.01;
                    newLon = currentLocation.getLongitude() + (random.nextDouble() - 0.5) * 0.01;
                    newLat = Math.max(OTTAWA_LAT_MIN, Math.min(OTTAWA_LAT_MAX, newLat));
                    newLon = Math.max(OTTAWA_LON_MIN, Math.min(OTTAWA_LON_MAX, newLon));

                    double distance = calculateDistance(oldLat, oldLon, newLat, newLon);

                    if (distance > 0) {
                        double fuelUsed = (distance / 100) * vehicle.getConsumptionRate();

                        ConsumptionLog consumptionLog = new ConsumptionLog();
                        consumptionLog.setVehicleId(vehicle.getVehicleId());
                        consumptionLog.setLogDate(new Date(System.currentTimeMillis()));
                        consumptionLog.setUsageAmount(fuelUsed);

                        if (consumptionDAO.addConsumptionLog(consumptionLog)) {
                            consumptionLogsCreated++;
                        }
                    }
                } else {
                    newLat = OTTAWA_LAT_MIN + random.nextDouble() * (OTTAWA_LAT_MAX - OTTAWA_LAT_MIN);
                    newLon = OTTAWA_LON_MIN + random.nextDouble() * (OTTAWA_LON_MAX - OTTAWA_LON_MIN);

                    ConsumptionLog initialLog = new ConsumptionLog();
                    initialLog.setVehicleId(vehicle.getVehicleId());
                    initialLog.setLogDate(new Date(System.currentTimeMillis()));
                    initialLog.setUsageAmount(vehicle.getConsumptionRate() * 0.05);

                    if (consumptionDAO.addConsumptionLog(initialLog)) {
                        consumptionLogsCreated++;
                    }
                }

                VehicleLocation newLocation = new VehicleLocation();
                newLocation.setVehicleId(vehicle.getVehicleId());
                newLocation.setLatitude(newLat);
                newLocation.setLongitude(newLon);
                newLocation.setTimestamp(new Timestamp(System.currentTimeMillis()));

                if (locationsDAO.updateLocation(newLocation)) {
                    updatedCount++;
                }
            }

            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<!DOCTYPE html><html><head>"
                    + "<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>GPS Simulation</title>"
                    + "<link rel='stylesheet' href='commands.css'>"
                    + "</head><body><div class='container'>"
                    + "<h1>GPS Simulation Complete</h1>"
                    + "<p class='success'>Updated " + updatedCount + " vehicle locations.</p>"
                    + "<p class='success'>Created " + consumptionLogsCreated + " consumption logs.</p>"
                    + "<p>Vehicles have moved and fuel consumption has been recorded based on distance traveled.</p>"
                    + "<a href='gpsDashboard.jsp' class='btn'>Back to GPS Dashboard</a>"
                    + "<a href='controller?action=generateReport&reportType=fuelCost' class='btn'>View Fuel Cost Report</a>"
                    + "</div></body></html>");

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return "/error.html";
        }
    }

    /**
     * Calculates distance between two coordinates using the Haversine formula.
     *
     * @return distance in kilometers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
