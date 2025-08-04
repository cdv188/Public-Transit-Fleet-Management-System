package viewlayer;

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
 * ShowVehicleByIdServlet - Shows vehicle details
 * Accessible by all authenticated users (Managers and Operators)
 * @author Chester
 */
public class ShowVehicleByIdServlet extends HttpServlet {

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
     * Processes requests for both HTTP GET and POST methods.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is authenticated
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String vehicleIdStr = request.getParameter("id");

        try {
            int vehicleId = Integer.parseInt(vehicleIdStr);
            VehicleLogic logic = new VehicleLogic();
            Vehicle vehicle = logic.getVehicleById(vehicleId);

            if (vehicle == null) {
                response.sendRedirect("ShowVehicleList?error=notfound");
                return;
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
        return "Vehicle Details Servlet - Authenticated Access";
    }
}