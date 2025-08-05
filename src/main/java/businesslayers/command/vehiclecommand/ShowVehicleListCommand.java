package businesslayers.command.vehiclecommand;

import dataaccesslayer.users.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dataaccesslayer.vehicles.VehicleLogic;
import businesslayers.builder.Vehicle;
import businesslayers.command.Command;

/**
 * Command for displaying the list of vehicles.
 * Accessible to all authenticated users; Manager role adds extra actions.
 */
public class ShowVehicleListCommand implements Command {

    /** @return true if the user is logged in. */
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    /** @return true if the logged-in user has the Manager role. */
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

        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            VehicleLogic logic = new VehicleLogic();
            List<Vehicle> vehicles = logic.getAllVehicles();

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

            request.getRequestDispatcher("/views/vehicle-list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load vehicle list");
            request.getRequestDispatcher("/views/vehicle-list.jsp").forward(request, response);
        }
    }
}