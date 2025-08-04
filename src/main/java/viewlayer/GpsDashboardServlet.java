package viewlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vehicelDAO.VehicleDAO;
import vehicelDAO.VehicleDAOImpl;
import dao.VehicleLocationsDAO;
import dao.VehicleLocationsDAOImpl;
import vehicleSimpleFactory.Vehicle;
import dto.VehicleLocation;

/**
 * GPS Dashboard Servlet - Displays real-time GPS data for all vehicles
 * @author Ali
 */
public class GpsDashboardServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            VehicleDAO vehicleDAO = new VehicleDAOImpl();
            VehicleLocationsDAO locationDAO = new VehicleLocationsDAOImpl();

            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            List<VehicleLocation> locations = locationDAO.getAllCurrentLocations();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>GPS Dashboard - PTFMS</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link rel=\"stylesheet\" href=\"commands.css\">  ");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>GPS Dashboard</h1>");
            out.println("<p>Vehicle locations are shown in the table below.</p>");
            out.println("<a href='" + request.getContextPath() + "/simulateGPS' class='btn'>Simulate GPS Updates</a>");

            out.println("<table border='1'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Vehicle Number</th>");
            out.println("<th>Vehicle Type</th>");
            out.println("<th>Route</th>");
            out.println("<th>Latitude</th>");
            out.println("<th>Longitude</th>");
            out.println("<th>Last Updated</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            for (Vehicle vehicle : vehicles) {
                VehicleLocation location = locations.stream()
                        .filter(loc -> loc.getVehicleId() == vehicle.getVehicleId())
                        .findFirst().orElse(null);

                out.println("<tr>");
                out.println("<td>" + vehicle.getNumber() + "</td>");
                out.println("<td>" + vehicle.getVehicleType() + "</td>");
                out.println("<td>" + vehicle.getRoute() + "</td>");

                if (location != null) {
                    out.println("<td>" + String.format("%.6f", location.getLatitude()) + "</td>");
                    out.println("<td>" + String.format("%.6f", location.getLongitude()) + "</td>");
                    out.println("<td>" + location.getTimestamp() + "</td>");
                    out.println("<td style='color: green;'>Active</td>");
                } else {
                    out.println("<td colspan='3' style='text-align: center;'>No GPS data available</td>");
                    out.println("<td style='color: red;'>Offline</td>");
                }
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("<p><strong>Total Vehicles:</strong> " + vehicles.size() + "</p>");
            out.println("<p><strong>Vehicles with GPS:</strong> " + locations.size() + "</p>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "GPS Dashboard Servlet";
    }
}
