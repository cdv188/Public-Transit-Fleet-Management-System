package businesslayers.command.maintenanceCommand;

import businesslayers.command.Command;
import dataaccesslayer.maintenance.MaintenanceLog;
import dataaccesslayer.maintenance.MaintenanceLogLogic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dataaccesslayer.users.User;

/**
 * Command for displaying and managing the maintenance dashboard.
 */
public class ShowMaintenanceCommand implements Command {

    private final MaintenanceLogLogic logic = new MaintenanceLogLogic();
    private MaintenanceDashboardCommand schedule = new MaintenanceDashboardCommand();

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
        String action = request.getParameter("action");

        if ("POST".equalsIgnoreCase(method)) {
            handlePost(request, response, action);
        } else {
            handleGet(request, response, action);
        }
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response, String action)
            throws ServletException, IOException {

        if ("schedule".equals(action)) {
            if (!isManager(request)) {
                response.sendRedirect("FrontController?action=showMaintenance&error=unauthorized");
                return;
            }
            schedule.execute(request, response);
        } else {
            displayDashboard(request, response);
        }
    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response, String action)
            throws ServletException, IOException {

        if ("schedule".equals(action) || "update".equals(action)) {
            if (!isManager(request)) {
                response.sendRedirect("FrontController?action=showMaintenance&error=unauthorized");
                return;
            }
            if ("schedule".equals(action)) {
                schedule.execute(request, response);
            } else {
                schedule.updateMaintenanceTask(request, response);
            }
        } else {
            response.sendRedirect("FrontController?action=showMaintenance&error=invalid");
        }
    }

    /** Displays the maintenance dashboard with categorized task lists. */
    private void displayDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<MaintenanceLog> allMaintenanceLogs = logic.getAlarmsAndTasks();
            List<MaintenanceLog> alerts = new ArrayList<>();
            List<MaintenanceLog> scheduledTasks = new ArrayList<>();
            List<MaintenanceLog> inProgressTasks = new ArrayList<>();
            List<MaintenanceLog> completedTasks = new ArrayList<>();

            for (MaintenanceLog log : allMaintenanceLogs) {
                switch (log.getStatus()) {
                    case "Alert":
                        alerts.add(log);
                        break;
                    case "Scheduled":
                        scheduledTasks.add(log);
                        break;
                    case "In-Progress":
                        inProgressTasks.add(log);
                        break;
                    case "Completed":
                        if (completedTasks.size() < 10) {
                            completedTasks.add(log);
                        }
                        break;
                }
            }

            request.setAttribute("alerts", alerts);
            request.setAttribute("scheduledTasks", scheduledTasks);
            request.setAttribute("inProgressTasks", inProgressTasks);
            request.setAttribute("completedTasks", completedTasks);
            request.setAttribute("allMaintenanceLogs", allMaintenanceLogs);
            request.setAttribute("isManager", isManager(request));

            request.getRequestDispatcher("/views/maintenance-dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load maintenance dashboard");
            request.getRequestDispatcher("/views/maintenance-dashboard.jsp").forward(request, response);
        }
    }
}