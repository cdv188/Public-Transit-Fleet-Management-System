package viewlayer;

import command.DeleteVehicleCommand;
import userDAO.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vehicelDAO.VehicleLogic;
import vehicleSimpleFactory.Vehicle;

/**
 * ShowVehicleByIdServlet - Shows vehicle details and handles delete operations
 * Accessible by all authenticated users (Managers and Operators)
 * Delete functionality only available to Managers
 * @author Chester
 */
public class ShowVehicleByIdServlet extends HttpServlet {
    private DeleteVehicleCommand deleteCommand = new DeleteVehicleCommand();

    /**
     * Check if user is authenticated
     */
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

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
     * Handles GET requests - Shows vehicle details
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is authenticated
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String vehicleIdStr = request.getParameter("id");
        String success = request.getParameter("success");

        try {
            int vehicleId = Integer.parseInt(vehicleIdStr);
            VehicleLogic logic = new VehicleLogic();
            Vehicle vehicle = logic.getVehicleById(vehicleId);

            if (vehicle == null) {
                response.sendRedirect("ShowVehicleList?error=notfound");
                return;
            }

            // Set success message if coming from update
            if ("updated".equals(success)) {
                request.setAttribute("successMessage", "Vehicle updated successfully!");
            }

            // Set attributes for JSP
            request.setAttribute("vehicle", vehicle);
            request.setAttribute("isManager", isManager(request));

            // Forward to JSP
            request.getRequestDispatcher("/views/vehicle-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowVehicleList?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load vehicle details");
            response.sendRedirect("ShowVehicleList?error=system");
        }
    }

    /**
     * Handles POST requests - Processes delete operations
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is authenticated and is a Manager
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        if (!isManager(request)) {
            response.sendRedirect("ShowVehicleList?error=unauthorized");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            deleteCommand.execute(request, response);
        } else {
            response.sendRedirect("ShowVehicleList?error=invalidaction");
        }
    }

    @Override
    public String getServletInfo() {
        return "Vehicle Details Servlet - Authenticated Access, Manager Delete";
    }
}