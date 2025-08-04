package viewlayer;

import command.UpdateVehicleCommand;
import userDAO.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * UpdateVehicleServlet - Handles vehicle updates
 * Only accessible by Managers
 * @author Chester
 */
public class UpdateVehicleServlet extends HttpServlet {
    private UpdateVehicleCommand updateCommand = new UpdateVehicleCommand();

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
     * Handles the HTTP GET method - Shows update form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is a Manager
        if (!isManager(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        updateCommand.execute(request, response);
    }

    /**
     * Handles the HTTP POST method - Processes vehicle update
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is a Manager
        if (!isManager(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }
        
        // Validate required fields
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
           updateCommand.execute(request, response);
           return;
        }
        
        try {
           double consumptionRate = Double.parseDouble(consumptionRateStr.trim());
           int maxPassengers = Integer.parseInt(maxPassengersStr.trim());

           if (consumptionRate < 0 || maxPassengers <= 0) {
               request.setAttribute("errorMessage", "Invalid values: consumption rate cannot be negative and passengers must be positive");
               updateCommand.execute(request, response);
               return;
           }
           
           updateCommand.execute(request, response);
           
       } catch (NumberFormatException e) {
           request.setAttribute("errorMessage", "Please enter valid numbers for consumption rate and passengers");
           updateCommand.execute(request, response);
       }
    }

    @Override
    public String getServletInfo() {
        return "Vehicle Update Servlet - Manager Access Only";
    }
}