package businesslayers.command;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dataaccesslayer.vehicles.VehicleDAO;
import dataaccesslayer.vehicles.VehicleDAOImpl;
import businesslayers.builder.Vehicle;

/**
 * Command that handles deletion of a vehicle by its ID.
 */
public class DeleteVehicleCommand implements Command {

    private VehicleDAO vehicleDAO;

    /** Creates a new command with a default {@link VehicleDAOImpl}. */
    public DeleteVehicleCommand() {
        this.vehicleDAO = new VehicleDAOImpl();
    }

    /**
     * Executes the vehicle deletion process.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
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
