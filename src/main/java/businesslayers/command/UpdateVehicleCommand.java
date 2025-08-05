package businesslayers.command;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dataaccesslayer.vehicles.VehicleDAO;
import dataaccesslayer.vehicles.VehicleDAOImpl;
import businesslayers.builder.Vehicle;
import businesslayers.simplefactory.VehicleFactory;

/**
 * Command that updates vehicle details.
 */
public class UpdateVehicleCommand implements Command {

    private VehicleDAO vehicleDAO;
    private VehicleFactory vehicleFactory;

    /** Creates a new command with default DAO and factory implementations. */
    public UpdateVehicleCommand() {
        this.vehicleDAO = new VehicleDAOImpl();
        this.vehicleFactory = new VehicleFactory();
    }

    /**
     * Executes vehicle update logic for GET (form display) or POST (submission).
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            showUpdateForm(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            processUpdate(request, response);
        }
    }

    /** Displays the update form with existing vehicle data. */
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String vehicleIdStr = request.getParameter("vehicleId");
            if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty()) {
                response.sendRedirect("ShowVehicleList?error=invalid");
                return;
            }

            int vehicleId = Integer.parseInt(vehicleIdStr);
            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);

            if (vehicle == null) {
                response.sendRedirect("ShowVehicleList?error=notfound");
                return;
            }

            request.setAttribute("vehicle", vehicle);
            request.getRequestDispatcher("/views/update-vehicle.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowVehicleList?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ShowVehicleList?error=system");
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
                response.sendRedirect("ShowVehicleList?error=notfound");
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
            response.sendRedirect("ShowVehicleList?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ShowVehicleList?error=system");
        }
    }
}
