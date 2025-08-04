package command;

import dao.VehicleLocationsDAO;
import dao.VehicleLocationsDAOImpl;
import dto.VehicleLocation;
import vehicelDAO.VehicleLogic;
import vehicleSimpleFactory.Vehicle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

public class SimulateGPSCommand implements Command {

    private static final double OTTAWA_LAT_MIN = 45.25;
    private static final double OTTAWA_LAT_MAX = 45.50;
    private static final double OTTAWA_LON_MIN = -76.00;
    private static final double OTTAWA_LON_MAX = -75.50;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        executeWithResult(request, response);
    }

    public String executeWithResult(HttpServletRequest request, HttpServletResponse response) {
        try {
            VehicleLogic vehicleLogic = new VehicleLogic();
            VehicleLocationsDAO locationsDAO = new VehicleLocationsDAOImpl();

            List<Vehicle> vehicles = vehicleLogic.getAllVehicles();
            Random random = new Random();
            int updatedCount = 0;

            for (Vehicle vehicle : vehicles) {
                VehicleLocation currentLocation = locationsDAO.getCurrentLocation(vehicle.getVehicleId());
                double newLat, newLon;

                if (currentLocation != null) {
                    newLat = currentLocation.getLatitude() + (random.nextDouble() - 0.5) * 0.01;
                    newLon = currentLocation.getLongitude() + (random.nextDouble() - 0.5) * 0.01;
                    newLat = Math.max(OTTAWA_LAT_MIN, Math.min(OTTAWA_LAT_MAX, newLat));
                    newLon = Math.max(OTTAWA_LON_MIN, Math.min(OTTAWA_LON_MAX, newLon));
                } else {
                    newLat = OTTAWA_LAT_MIN + random.nextDouble() * (OTTAWA_LAT_MAX - OTTAWA_LAT_MIN);
                    newLon = OTTAWA_LON_MIN + random.nextDouble() * (OTTAWA_LON_MAX - OTTAWA_LON_MIN);
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
                    + "<link rel='stylesheet' href='css/commands.css'>"
                    + "</head><body><div class='container'>"
                    + "<h1>GPS Simulation</h1>"
                    + "<p class='success'>GPS simulation complete. Updated " + updatedCount + " vehicle locations.</p>"
                    + "</div></body></html>");

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return "/error.html";
        }
    }
}
