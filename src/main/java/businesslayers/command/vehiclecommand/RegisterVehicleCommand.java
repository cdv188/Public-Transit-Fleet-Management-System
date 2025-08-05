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
 * Command for handling vehicle registration, accessible only by Managers.
 */
public class RegisterVehicleCommand implements Command {

    private VehicleDAO vehicleDAO;
    private VehicleFactory vehicleFactory;

    /** Creates a new command with default DAO and factory implementations. */
    public RegisterVehicleCommand() {
        this.vehicleDAO = new VehicleDAOImpl();
        this.vehicleFactory = new VehicleFactory();
    }

    /** Checks if the current user is a Manager. */
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
            handleGet(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(request, response);
        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String error = request.getParameter("error");
        String success = request.getParameter("successMessage");

        if (error != null) {
            request.setAttribute("errorMessage", getErrorMessage(error));
        }
        if (success != null) {
            request.setAttribute("successMessage", "Vehicle Successfully Added");
        }

        request.getRequestDispatcher("/views/register-vehicle.jsp").forward(request, response);
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vehicleNumber = request.getParameter("vehicleNumber");
        String vehicleType = request.getParameter("vehicleType");
        String fuelType = request.getParameter("fuelType");
        String consumptionRateStr = request.getParameter("consumptionRate");
        String maxPassengersStr = request.getParameter("maxPassengers");
        String route = request.getParameter("assignedRoute");
        String action = request.getParameter("action");

        if (vehicleNumber == null || vehicleNumber.trim().isEmpty() ||
            vehicleType == null || vehicleType.trim().isEmpty() ||
            fuelType == null || fuelType.trim().isEmpty() ||
            consumptionRateStr == null || consumptionRateStr.trim().isEmpty() ||
            maxPassengersStr == null || maxPassengersStr.trim().isEmpty() ||
            route == null || route.trim().isEmpty()) {

            request.setAttribute("errorMessage", "All fields must not be empty");
            request.getRequestDispatcher("/views/register-vehicle.jsp").forward(request, response);
            return;
        }

        try {
            double consumptionRate = Double.parseDouble(consumptionRateStr.trim());
            int maxPassengers = Integer.parseInt(maxPassengersStr.trim());

            if (consumptionRate < 0 || maxPassengers < 0) {
                request.setAttribute("errorMessage", "Values cannot be negative");
                request.getRequestDispatcher("/views/register-vehicle.jsp").forward(request, response);
                return;
            }

            if ("register".equals(action)) {
                processRegistration(request, response, vehicleNumber, vehicleType, fuelType, 
                                  consumptionRate, maxPassengers, route);
            } else {
                request.setAttribute("errorMessage", "Invalid action");
                request.getRequestDispatcher("/views/register-vehicle.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Please enter valid numbers for consumption rate and passengers");
            request.getRequestDispatcher("/views/register-vehicle.jsp").forward(request, response);
        }
    }

    private void processRegistration(HttpServletRequest request, HttpServletResponse response,
                                   String vehicleNumber, String vehicleType, String fuelType,
                                   double consumptionRate, int maxPassengers, String route)
            throws ServletException, IOException {
        try {
            if (vehicleDAO.getVehicleByNumber(vehicleNumber.trim()) != null) {
                response.sendRedirect("RegisterVehicle?error=exist");
                return;
            }

            if (maxPassengers <= 0) {
                request.setAttribute("errorMessage", "Invalid maximum passengers value");
                request.getRequestDispatcher("/views/register-vehicle.jsp").forward(request, response);
                return;
            }

            Vehicle newVehicle = vehicleFactory.createVehicle(
                    vehicleNumber.trim(),
                    vehicleType,
                    fuelType.trim(),
                    maxPassengers,
                    consumptionRate,
                    route.trim()
            );

            boolean success = vehicleDAO.addVehicle(newVehicle);

            if (success) {
                response.sendRedirect("RegisterVehicle?successMessage=success");
            } else {
                response.sendRedirect("RegisterVehicle?error=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("RegisterVehicle?error=failed");
        }
    }

    /** Returns the error message for a given code. */
    private String getErrorMessage(String error) {
        switch (error) {
            case "emptyFields": return "All fields must not be empty";
            case "failed": return "Failed to Register";
            case "exist": return "Vehicle number already exists";
            default: return "An error occurred";
        }
    }
}