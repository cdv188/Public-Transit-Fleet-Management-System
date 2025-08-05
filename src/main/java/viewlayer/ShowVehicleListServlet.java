package viewlayer;

import dataaccesslayer.users.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dataaccesslayer.vehicles.VehicleLogic;
import businesslayers.builder.Vehicle;

/**
 * Displays list of vehicles
 */
public class ShowVehicleListServlet extends HttpServlet {

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
     * Processes requests for both GET and POST request.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is authenticated
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            VehicleLogic logic = new VehicleLogic();
            List<Vehicle> vehicles = logic.getAllVehicles();
            
            // Set attributes for role
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("isManager", isManager(request));
            
            HttpSession session = request.getSession(false);
            if (session != null) {
                String successMessage = (String) session.getAttribute("successMessage");
                if (successMessage != null) {
                    request.setAttribute("successMessage", successMessage);
                    session.removeAttribute("successMessage");
                }
            }
            
            // Forward to vehicle list
            request.getRequestDispatcher("/views/vehicle-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load vehicle list");
            request.getRequestDispatcher("/views/vehicle-list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Vehicle List Servlet - Authenticated Access";
    }
}