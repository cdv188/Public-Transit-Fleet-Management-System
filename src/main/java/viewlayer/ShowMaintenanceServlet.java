package viewlayer;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogDAO;
import MaintenanceLogDAO.MaintenanceLogLogic;
import command.MaintenanceDashboardCommand;
import command.ScheduleMaintenanceCommand;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vehicelDAO.VehicleDAO;

/**
 *
 * @author Chester
 */
public class ShowMaintenanceServlet extends HttpServlet {
    private MaintenanceLogLogic logic = new MaintenanceLogLogic();
    private ScheduleMaintenanceCommand schedule;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.schedule = new ScheduleMaintenanceCommand();
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String successMessage = (String) request.getSession().getAttribute("successMessage");
        
        // Fetch maintenance data
        List<MaintenanceLog> allMaintenanceLogs = logic.getAlarmsAndTasks();
        // separate lists for different status types
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
                // keep the first 10 completed tasks
                if (completedTasks.size() < 10) {
                    completedTasks.add(log);
                }
            }
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Maintenance Dashboard - PTFMS</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<link rel=\"stylesheet\" href=\"maintenance.css\">  ");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<div class='nav'>");
        out.println("<a href='ShowVehicleList'>Vehicle List</a>");
        out.println("<a href='ShowMaintenance?action=schedule'>Schedule Maintenance</a>");
        out.println("<a href='gpsDashboard'>GPS Dashboard</a>");
        out.println("<a href='reports'>Reports</a>");
        out.println("<a href='FrontController?action=runSystemChecks'>Run System Checks</a>");
        out.println("</div>");
        out.println("<h1>Maintenance Dashboard</h1>");

        if ("success".equals(request.getParameter("successMessage"))) {
            out.println("<div class='success'>Successfully Completed</div>");
        }
        if ("scheduledsuccess".equals(request.getParameter("successMessage"))) {
            out.println("<div class='success'>New Schedule Added</div>");
        }
        if("failed".equals(request.getParameter("error"))){
            out.println("<div class='error'>Failed to Register</div>");
        }
        if("taskstarted".equals(request.getParameter("successMessage"))){
            out.println("<div class='inprogress'>Task Started</div>");
        }
        out.println("<div class='dashboard-grid'>");
        
        // Alerts card
        out.println("<div class='card alert'>");
        out.println("<div class='card-title'>Active Alerts</div>");
        out.println("<div class='card-count'>" + alerts.size() + "</div>");
        out.println("<p>Required immediate attention</p>");
        out.println("</div>");
        
        // Scheduled tasks card
        out.println("<div class='card scheduled'>");
        out.println("<div class='card-title'>Scheduled Tasks</div>");
        out.println("<div class='card-count'>" + scheduledTasks.size() + "</div>");
        out.println("<p>Upcoming Mintenance</p>");
        out.println(" </div>");
        
        // In-progress tasks card
        out.println("<div class='card in-progress'>");
        out.println("<div class='card-title'>In Progress</div>");
        out.println("<div class='card-count'>" + inProgressTasks.size() + "</div>");
        out.println("<p>active maintenance</p>");
        out.println("</div>");
        
        // Completed tasks card
        out.println("<div class='card completed'>");
        out.println("<div class='card-title'>Completed</div>");
        out.println("<div class='card-count'>" + completedTasks.size() + "</div>");
        out.println("<p>Completed Maintenance</p>");
        out.println("</div>");
        
        out.println("</div>");
        
        // Quick actions
        out.println("<div style='margin: 30px 0;'>");
        out.println("<a href='ShowMaintenance?action=schedule' class='btn btn-success'>Schedule New Maintenance</a>");
        out.println("<a href='RegisterVehicle' class='btn'>Register Vehicle</a>");
        out.println("</div>");
        
        // Alerts section
        if (!alerts.isEmpty()) {
            out.println("<h2 style='color: #dc3545; margin-top: 40px;'>Active Alerts</h2>");
            out.println("<div style=\"overflow-x: auto;\">");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Vehicle</th>");
            out.println("<th>Description</th>");
            out.println("<th>Status</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            for (MaintenanceLog alert : alerts) {
                out.println("<tr>");
                out.println("<td>" + alert.getVehicleNumber() + "</td>");
                out.println("<td>" + alert.getTaskDescription() + "</td>");
                out.println("<td><span class='status-badge status-alert'>" + alert.getStatus() + "</span></td>");
                out.println("<td>");
                out.println("<a href='ShowMaintenanceById?id=" + alert.getTaskId() + "' class='btn'>View</a>");
                out.println("</td>");
                out.println("</tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
        }
        
        // Scheduled tasks section
        if (!scheduledTasks.isEmpty()) {
            out.println("<h2 style='margin-top: 40px;'>Scheduled Maintenance</h2>");
            out.println("<div style=\"overflow-x: auto;\">");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Vehicle</th>");
            out.println("<th>Description</th>");
            out.println("<th>Scheduled Date</th>");
            out.println("<th>Status</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            for (MaintenanceLog task : scheduledTasks) {
                out.println("<tr>");
                out.println("<td>" + task.getVehicleNumber() + "</td>");
                out.println("<td>" + task.getTaskDescription() + "</td>");
                out.println("<td>" + task.getScheduledDate() + "</td>");
                out.println("<td><span class='status-badge status-scheduled'>" + task.getStatus() + "</span></td>");
                out.println("<td>");
                out.println("<a href='ShowMaintenanceById?id=" + task.getTaskId() + "' class='btn'>View</a>");
                out.println("<form style='display:inline;' method='post' action='ShowMaintenance?action=update'>");
                out.println("<input type='hidden' name='taskId' value='" + task.getTaskId() + "'>");
                out.println("<input type='hidden' name='status' value='In-Progress'>");
                out.println("<button type='submit' class='btn btn-warning'>Start</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
        }
        
        // In-progress tasks section
        if (!inProgressTasks.isEmpty()) {
            out.println("<h2 style='margin-top: 40px;'>In Progress</h2>");
            out.println("<div style=\"overflow-x: auto;\">");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Vehicle</th>");
            out.println("<th>Description</th>");
            out.println("<th>Scheduled Date</th>");
            out.println("<th>Status</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            for (MaintenanceLog task : inProgressTasks) {
                out.println("<tr>");
                out.println("<td>" + task.getVehicleNumber() + "</td>");
                out.println("<td>" + task.getTaskDescription() + "</td>");
                out.println("<td>" + task.getScheduledDate() + "</td>");
                out.println("<td><span class='status-badge status-in-progress'>" + task.getStatus() + "</span></td>");
                out.println("<td>");
                out.println("<a href='ShowMaintenanceById?id=" + task.getTaskId() + "' class='btn'>View</a>");
                out.println("<form style='display:inline;' method='post' action='ShowMaintenance?action=update'>");
                out.println("<input type='hidden' name='taskId' value='" + task.getTaskId() + "'>");
                out.println("<input type='hidden' name='status' value='Completed'>");
                out.println("<button type='submit' class='btn btn-success'>Complete</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
        }
        
        // Recent completed tasks
        if (!completedTasks.isEmpty()) {
            out.println("<h2 style='margin-top: 40px;'>Recently Completed</h2>");
            out.println("<div style=\"overflow-x: auto;\">");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Vehicle</th>");
            out.println("<th>Description</th>");
            out.println("<th>Scheduled Date</th>");
            out.println("<th>Completion Date</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            for (MaintenanceLog task : completedTasks) {
                out.println("<tr>");
                out.println("<td>" + task.getVehicleNumber() + "</td>");
                out.println("<td>" + task.getTaskDescription() + "</td>");
                out.println("<td>" + task.getScheduledDate()  + "</td>");
                out.println("<td>" + task.getCompletionDate() + "</td>");
                out.println("<td><span class='status-badge status-completed'>" + task.getStatus() + "</span></td>");
                out.println("</tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
        }
        
        if (allMaintenanceLogs.isEmpty()) {
            out.println("<div style='text-align: center; margin: 40px 0;'>");
            out.println("<p>No maintenance records found.</p>");
            out.println("<a href='ScheduleNewMaintence' class='btn btn-success'>Schedule First Maintenance Task</a>");
            out.println("</div>");
        }
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("schedule".equals(action)) {
            schedule.execute(request, response);
        } else if ("update".equals(action)) {
            schedule.updateMaintenanceTask(request, response);
        } else {
            response.sendRedirect("ShowMaintenance?error=invalid");
        }
    }
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
    
        if ("schedule".equals(action)) {
            schedule.execute(request, response);
        } else {
            processRequest(request, response);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
