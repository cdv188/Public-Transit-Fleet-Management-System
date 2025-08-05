package businesslayers.command;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dataaccesslayer.vehicles.VehicleDAO;
import dataaccesslayer.vehicles.VehicleDAOImpl;
import businesslayers.builder.Vehicle;

/**
 * Handles vehicle deletion operations.
 */
public class DeleteVehicleCommand implements Command {
    private VehicleDAO vehicleDAO;
    
    public DeleteVehicleCommand() {
        this.vehicleDAO = new VehicleDAOImpl();
    }
    /**
     * Executes vehicle deletion requests.
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String vehicleIdStr = request.getParameter("vehicleId");
            
            if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty()) {
                response.sendRedirect("ShowVehicleList?error=invalid");
                return;
            }
            
            int vehicleId = Integer.parseInt(vehicleIdStr);
            Vehicle vehicle = vehicleDAO.getVehicleById(vehicleId);
            boolean success = vehicleDAO.deleteVehicle(vehicleId);
            
            if (success) {
                response.sendRedirect("ShowVehicleList?successMessage=deleted");
            } else {
                response.sendRedirect("ShowVehicleList?error=deletefailed");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("ShowVehicleList?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ShowVehicleList?error=system");
        }
    }
}