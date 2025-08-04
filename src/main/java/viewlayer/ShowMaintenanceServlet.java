package viewlayer;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogLogic;
import command.MaintenanceDashboardCommand;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import userDAO.User;

/**
 *
 * @author Chester
 */
public class ShowMaintenanceServlet extends HttpServlet {
    private final MaintenanceLogLogic logic = new MaintenanceLogLogic();
    private MaintenanceDashboardCommand schedule;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.schedule = new MaintenanceDashboardCommand();
    }

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
        
        try {
            // Fetch maintenance data
            List<MaintenanceLog> allMaintenanceLogs = logic.getAlarmsAndTasks();
            
            // Separate lists for different status types
            List<MaintenanceLog> alerts = new ArrayList<>();
            List<MaintenanceLog> scheduledTasks = new ArrayList<>();
            List<MaintenanceLog> inProgressTasks = new ArrayList<>();
            List<MaintenanceLog> completedTasks = new ArrayList<>();

            // Loop through all maintenance logs and sort them by status
            for (MaintenanceLog log : allMaintenanceLogs) {
                String status = log.getStatus();
                if (status.equals("Alert")) {
                    alerts.add(log);
                } else if (status.equals("Scheduled")) {
                    scheduledTasks.add(log);
                } else if (status.equals("In-Progress")) {
                    inProgressTasks.add(log);
                } else if (status.equals("Completed")) {
                    // Keep the first 10 completed tasks
                    if (completedTasks.size() < 10) {
                        completedTasks.add(log);
                    }
                }
            }

            // Set attributes for JSP
            request.setAttribute("alerts", alerts);
            request.setAttribute("scheduledTasks", scheduledTasks);
            request.setAttribute("inProgressTasks", inProgressTasks);
            request.setAttribute("completedTasks", completedTasks);
            request.setAttribute("allMaintenanceLogs", allMaintenanceLogs);
            request.setAttribute("isManager", isManager(request));
            
            // Forward to JSP
            request.getRequestDispatcher("/views/maintenance-dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load maintenance dashboard");
            request.getRequestDispatcher("/views/maintenance-dashboard.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP POST method
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is authenticated
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("schedule".equals(action)) {
            // Only Managers can schedule maintenance
            if (!isManager(request)) {
                response.sendRedirect("ShowMaintenance?error=unauthorized");
                return;
            }
            schedule.execute(request, response);
        } else if ("update".equals(action)) {
            // Only Managers can update maintenance tasks
            if (!isManager(request)) {
                response.sendRedirect("ShowMaintenance?error=unauthorized");
                return;
            }
            schedule.updateMaintenanceTask(request, response);
        } else {
            response.sendRedirect("ShowMaintenance?error=invalid");
        }
    }

    /**
     * Handles the HTTP GET method
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
    
        if ("schedule".equals(action)) {
            // Only Managers can access the schedule form
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
        return "Maintenance Dashboard Servlet - Authenticated Access";
    }
}

