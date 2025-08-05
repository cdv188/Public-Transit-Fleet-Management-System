package viewlayer;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogLogic;
import businesslayers.command.MaintenanceDashboardCommand;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dataaccesslayer.users.User;

/**
 * Servlet for displaying and managing the maintenance dashboard.
 */
public class ShowMaintenanceServlet extends HttpServlet {

    private final MaintenanceLogLogic logic = new MaintenanceLogLogic();
    private MaintenanceDashboardCommand schedule;

    @Override
    public void init() throws ServletException {
        super.init();
        this.schedule = new MaintenanceDashboardCommand();
    }

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

    /** Displays the maintenance dashboard with categorized task lists. */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("schedule".equals(action) || "update".equals(action)) {
            if (!isManager(request)) {
                response.sendRedirect("ShowMaintenance?error=unauthorized");
                return;
            }
            if ("schedule".equals(action)) {
                schedule.execute(request, response);
            } else {
                schedule.updateMaintenanceTask(request, response);
            }
        } else {
            response.sendRedirect("ShowMaintenance?error=invalid");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("schedule".equals(action)) {
            if (!isManager(request)) {
                response.sendRedirect("ShowMaintenance?error=unauthorized");
                return;
            }
            schedule.execute(request, response);
        } else {
            processRequest(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Displays and manages the maintenance dashboard (authenticated access).";
    }
}
