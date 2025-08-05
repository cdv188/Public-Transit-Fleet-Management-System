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
 * Command that handles vehicle registration.
 */
public class RegisterVehicleCommand implements Command {

    private VehicleDAO vehicleDAO;
    private VehicleFactory vehicleFactory;

    /** Creates a new command with default DAO and factory implementations. */
    public RegisterVehicleCommand() {
        this.vehicleDAO = new VehicleDAOImpl();
        this.vehicleFactory = new VehicleFactory();
    }

    /**
     * Executes vehicle registration logic for GET (form display) or POST (submission).
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            showRegistrationForm(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            processRegistration(request, response);
        }
    }

    /** Redirects to the vehicle registration form. */
    private void showRegistrationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("RegisterVehicle");
    }

    /** Processes vehicle registration form submission. */
    private void processRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String vehicleNumber = request.getParameter("vehicleNumber");
            String vehicleType = request.getParameter("vehicleType");
            String fuelType = request.getParameter("fuelType");
            double consumptionRate = Double.parseDouble(request.getParameter("consumptionRate"));
            int maxPassengers = Integer.parseInt(request.getParameter("maxPassengers"));
            String assignedRoute = request.getParameter("assignedRoute");

            if (vehicleDAO.getVehicleByNumber(vehicleNumber.trim()) != null) {
                response.sendRedirect("RegisterVehicle?error=exist");
                return;
            }

            if (maxPassengers <= 0) {
                request.setAttribute("error", "Invalid maximum passengers value");
                return;
            }

            Vehicle newVehicle = vehicleFactory.createVehicle(
                    vehicleNumber.trim(),
                    vehicleType,
                    fuelType.trim(),
                    maxPassengers,
                    consumptionRate,
                    assignedRoute.trim()
            );

            boolean success = vehicleDAO.addVehicle(newVehicle);

            if (success) {
                response.sendRedirect("RegisterVehicle?successMessage=success");
            } else {
                response.sendRedirect("RegisterVehicle?error=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
