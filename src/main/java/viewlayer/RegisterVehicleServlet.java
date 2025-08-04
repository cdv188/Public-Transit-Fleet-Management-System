package viewlayer;

import command.RegisterVehicleCommand;
import userDAO.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * RegisterVehicleServlet - Handles vehicle registration
 * Only accessible by Managers
 * @author Chester
 */
public class RegisterVehicleServlet extends HttpServlet {
    private RegisterVehicleCommand registerCommand;

    /**
     * Check if user has Manager role
     */
    private boolean isManager(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return "Manager".equals(user.getUserType());
        }
        return false;
    }

    /**
     * Handles the HTTP GET method - Shows registration form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is a Manager
        if (!isManager(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        // Clear any previous form data and messages for fresh form load
        // Only preserve messages if explicitly passed
        String error = request.getParameter("error");
        String success = request.getParameter("successMessage");
        
        if (error != null) {
            request.setAttribute("errorMessage", getErrorMessage(error));
        }
        
        if (success != null) {
            request.setAttribute("successMessage", "Vehicle Successfully Added");
        }
        
        // Forward to JSP page
        request.getRequestDispatcher("/views/register-vehicle.jsp").forward(request, response);
    }

    /**
     * Get error message
     */
    private String getErrorMessage(String error) {
        switch (error) {
            case "emptyFields":
                return "All fields must not be empty";
            case "failed":
                return "Failed to Register";
            case "exist":
                return "Vehicle number already exists";
            default:
                return "An error occurred";
        }
    }

    /**
     * Handles the HTTP POST method - Processes vehicle registration
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is a Manager
        if (!isManager(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        String vehicleNumber = request.getParameter("vehicleNumber");
        String vehicleType = request.getParameter("vehicleType");
        String fuelType = request.getParameter("fuelType");
        String consumptionRateStr = request.getParameter("consumptionRate");
        String maxPassengersStr = request.getParameter("maxPassengers");
        String route = request.getParameter("assignedRoute");
        
        String action = request.getParameter("action");
        
        // Validate required fields
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty() || 
           vehicleType == null || vehicleType.trim().isEmpty() || 
           fuelType == null || fuelType.trim().isEmpty() ||
           consumptionRateStr == null || consumptionRateStr.trim().isEmpty() ||
           maxPassengersStr == null || maxPassengersStr.trim().isEmpty() ||
           route == null || route.trim().isEmpty()) {

           // Set error message as request attribute and forward back to form
           request.setAttribute("errorMessage", "All fields must not be empty");
           request.getRequestDispatcher("/register-vehicle.jsp").forward(request, response);
           return;
        }
        
        try {
           double consumptionRate = Double.parseDouble(consumptionRateStr.trim());
           int maxPassengers = Integer.parseInt(maxPassengersStr.trim());

           if (consumptionRate < 0 || maxPassengers < 0) {
               request.setAttribute("errorMessage", "Values cannot be negative");
               request.getRequestDispatcher("/register-vehicle.jsp").forward(request, response);
               return;
           }
           
           if ("register".equals(action)) {
                registerCommand.execute(request, response);
           } else {
               request.setAttribute("errorMessage", "Invalid action");
               request.getRequestDispatcher("/register-vehicle.jsp").forward(request, response);
           }
           
       } catch (NumberFormatException e) {
           request.setAttribute("errorMessage", "Please enter valid numbers for consumption rate and passengers");
           request.getRequestDispatcher("/register-vehicle.jsp").forward(request, response);
       }
    }

    @Override
    public String getServletInfo() {
        return "Vehicle Registration Servlet - Manager Access Only";
    }
}