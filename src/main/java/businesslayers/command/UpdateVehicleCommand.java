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
 * Handles vehicle update operations.
 */
public class UpdateVehicleCommand implements Command {
    private VehicleDAO vehicleDAO;
    private VehicleFactory vehicleFactory;
    
    public UpdateVehicleCommand() {
        this.vehicleDAO = new VehicleDAOImpl();
        this.vehicleFactory = new VehicleFactory();
    }
    /**
     * Executes GET or POST requests for vehicle updates.
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
    
    /**
     * Shows the update form with pre-filled vehicle data
     */
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
    
    /**
     * Processes the vehicle update form submission
     */
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
            
            // Check if vehicle exists
            Vehicle existingVehicle = vehicleDAO.getVehicleById(vehicleId);
            if (existingVehicle == null) {
                response.sendRedirect("ShowVehicleList?error=notfound");
                return;
            }
            
            // Check if vehicle number is being changed and if new number already exists
            if (!existingVehicle.getNumber().equals(vehicleNumber.trim())) {
                Vehicle duplicateCheck = vehicleDAO.getVehicleByNumber(vehicleNumber.trim());
                if (duplicateCheck != null) {
                    request.setAttribute("vehicle", existingVehicle);
                    request.setAttribute("errorMessage", "Vehicle number already exists");
                    request.getRequestDispatcher("/views/update-vehicle.jsp").forward(request, response);
                    return;
                }
            }
            
            // Validate positive values
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
            
            // Create updated vehicle object
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