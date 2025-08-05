package businesslayers.command.vehiclecommand;

import dataaccesslayer.users.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dataaccesslayer.vehicles.VehicleLogic;
import businesslayers.builder.Vehicle;
import businesslayers.command.Command;

/**
 * Command for displaying vehicle details and processing delete operations.
 * Accessible to all authenticated users; delete requires Manager role.
 */
public class ShowVehicleByIdCommand implements Command {

    private final DeleteVehicleCommand deleteCommand = new DeleteVehicleCommand();

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

        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method)) {
            handlePost(request, response);
        } else {
            handleGet(request, response);
        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vehicleIdStr = request.getParameter("id");
        String success = request.getParameter("success");

        try {
            int vehicleId = Integer.parseInt(vehicleIdStr);
            VehicleLogic logic = new VehicleLogic();
            Vehicle vehicle = logic.getVehicleById(vehicleId);

            if (vehicle == null) {
                response.sendRedirect("FrontController?action=showVehicleList&error=notfound");
                return;
            }

            if ("updated".equals(success)) {
                request.setAttribute("successMessage", "Vehicle updated successfully!");
            }

            request.setAttribute("vehicle", vehicle);
            request.setAttribute("isManager", isManager(request));
            request.getRequestDispatcher("/views/vehicle-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("FrontController?action=showVehicleList&error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FrontController?action=showVehicleList&error=system");
        }
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isManager(request)) {
            response.sendRedirect("FrontController?action=showVehicleList&error=unauthorized");
            return;
        }

        String action = request.getParameter("deleteAction");
        if ("delete".equals(action)) {
            deleteCommand.execute(request, response);
        } else {
            response.sendRedirect("FrontController?action=showVehicleList&error=invalidaction");
        }
    }
}