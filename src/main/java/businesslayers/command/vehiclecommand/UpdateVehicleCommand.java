package businesslayers.command.vehiclecommand;

import dataaccesslayer.users.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dataaccesslayer.vehicles.VehicleDAO;
import dataaccesslayer.vehicles.VehicleDAOImpl;
import businesslayers.builder.Vehicle;
import businesslayers.command.Command;
import businesslayers.simplefactory.VehicleFactory;

/**
 * Command for updating vehicle details.
 * Accessible only by Managers.
 */
public class UpdateVehicleCommand implements Command {

    private VehicleDAO vehicleDAO;
    private VehicleFactory vehicleFactory;

    /** Creates a new command with default DAO and factory implementations. */
    public UpdateVehicleCommand() {
        this.vehicleDAO = new VehicleDAOImpl();
        this.vehicleFactory = new VehicleFactory();
    }

    /** @return true if the logged-in user has the Manager role. */
    private boolean isManager(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return "Manager".equals(user.getUserType());
        }
        return false;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isManager(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }

        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            showUpdateForm(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(request, response);
        }
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vehicleNumber = request.getParameter("vehicleNumber");
        String vehicleType = request.getParameter("vehicleType");
        String fuelType = request.getParameter("fuelType");
        String consumptionRateStr = request.getParameter("consumptionRate");
        String maxPassengersStr = request.getParameter("maxPassengers");
        String route = request.getParameter("assignedRoute");

        if (vehicleNumber == null || vehicleNumber.trim().isEmpty() ||
            vehicleType == null || vehicleType.trim().isEmpty() ||
            fuelType == null || fuelType.trim().isEmpty() ||
            consumptionRateStr == null || consumptionRateStr.trim().isEmpty() ||
            maxPassengersStr == null || maxPassengersStr.trim().isEmpty() ||
            route == null || route.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All fields must not be empty");
            showUpdateForm(request, response);
            return;
        }

        try {
            double consumptionRate = Double.parseDouble(consumptionRateStr.trim());
            int maxPassengers = Integer.parseInt(maxPassengersStr.trim());

            if (consumptionRate < 0 || maxPassengers <= 0) {
                request.setAttribute("errorMessage",
                        "Invalid values: consumption rate cannot be negative and passengers must be positive");
                showUpdateForm(request, response);
                return;
            }

            processUpdate(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage",
                    "Please enter valid numbers for consumption rate and passengers");
            showUpdateForm(request, response);
        }
    }

    /** Displays the update form with existing vehicle data. */
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String vehicleIdStr = request.getParameter("vehicleId");
            if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty()) {
                response.sendRedirect("ShowVehicleById?error=invalid");
                return;
            }

            int vehicleId = Integer.parseInt(vehicleIdStr);
            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

            if (vehicle == null) {
                response.sendRedirect("ShowVehicleById?error=notfound");
                return;
            }

            request.setAttribute("vehicle", vehicle);
            request.getRequestDispatcher("/views/update-vehicle.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowVehicleById?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ShowVehicleById?error=system");
        }
    }

    /** Processes vehicle update form submission. */
    private void processUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            String vehicleNumber = request.getParameter("vehicleNumber");
            String vehicleType = request.getParameter("vehicleType");
            String fuelType = request.getParameter("fuelType");
            double consumptionRate = Double.parseDouble(request.getParameter("consumptionRate"));
            int maxPassengers = Integer.parseInt(request.getParameter("maxPassengers"));
            String assignedRoute = request.getParameter("assignedRoute");

            Vehicle existingVehicle = vehicleDAO.getVehicleById(vehicleId);
            if (existingVehicle == null) {
                response.sendRedirect("ShowVehicleById?error=notfound");
                return;
            }

            if (!existingVehicle.getNumber().equals(vehicleNumber.trim())) {
                Vehicle duplicateCheck = vehicleDAO.getVehicleByNumber(vehicleNumber.trim());
                if (duplicateCheck != null) {
                    request.setAttribute("vehicle", existingVehicle);
                    request.setAttribute("errorMessage", "Vehicle number already exists");
                    request.getRequestDispatcher("/views/update-vehicle.jsp").forward(request, response);
                    return;
                }
            }

            if (maxPassengers <= 0) {
                request.setAttribute("vehicle", existingVehicle);
                request.setAttribute("errorMessage", "Maximum passengers must be positive");
                request.getRequestDispatcher("/views/update-vehicle.jsp").forward(request, response);
                return;
            }

            if (consumptionRate < 0) {
                request.setAttribute("vehicle", existingVehicle);
                request.setAttribute("errorMessage", "Consumption rate cannot be negative");
                request.getRequestDispatcher("/views/update-vehicle.jsp").forward(request, response);
                return;
            }

            Vehicle updatedVehicle = Vehicle.builder()
                    .vehicleNumber(vehicleNumber.trim())
                    .vehicleType(vehicleType)
                    .fuelType(fuelType)
                    .consumptionRate(consumptionRate)
                    .maxCapacity(maxPassengers)
                    .route(assignedRoute.trim())
                    .vehicleId(vehicleId)
                    .build();

            boolean success = vehicleDAO.updateVehicle(updatedVehicle);

            if (success) {
                response.sendRedirect("ShowVehicleById?id=" + vehicleId + "&success=updated");
            } else {
                request.setAttribute("vehicle", existingVehicle);
                request.setAttribute("errorMessage", "Failed to update vehicle");
                request.getRequestDispatcher("/views/update-vehicle.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowVehicleById?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ShowVehicleById?error=system");
        }
    }
}
