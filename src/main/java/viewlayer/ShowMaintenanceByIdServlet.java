package viewlayer;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogLogic;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Chester
 */
public class ShowMaintenanceByIdServlet extends HttpServlet {
    private MaintenanceLogLogic logic = new MaintenanceLogLogic();

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
         String taskIdStr = request.getParameter("id");
         try {
            int taskId = Integer.parseInt(taskIdStr);
            MaintenanceLog task = logic.getMaintenanceLogById(taskId);

            if (task == null) {
                response.sendRedirect("ShowMaintenance?action=schedule");
                return;
            }

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Maintenance Task Details - " +task.getVehicleNumber() + "</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link rel=\"stylesheet\" href=\"maintenance.css\">  ");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container-id'>");
            out.println("<div class='nav'>");
            out.println("<a href='ShowMaintenance'>Back to Dashboard</a>");
            out.println("<a href='ShowVehicleList'>Vehicle List</a>");
            out.println("</div>");
            out.println("<h1>Maintenance Task Details</h1>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Task ID:</span>");
            out.println("<span class='detail-value'>" + task.getTaskId() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Vehicle:</span>");
            out.println("<span class='detail-value'>" + task.getVehicleNumber() + " (ID: " + task.getVehicleId() + ")</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Task Description:</span>");
            out.println("<span class='detail-value'>" + task.getTaskDescription() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Status:</span>");
            out.println("<span class='detail-value'>");
            out.println("<span class='status-badge status-" + task.getStatus().toLowerCase().replace("-", "") + "'>" + task.getStatus() + "</span>");
            out.println("</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Scheduled Date:</span>");
            out.println("<span class='detail-value'>" + (task.getScheduledDate() != null ? task.getScheduledDate().toString() : "Not Set") + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Completion Date:</span>");
            out.println("<span class='detail-value'>" +  (task.getCompletionDate() != null ? task.getCompletionDate().toString() : "Not Completed") + "</span>");
            out.println("</div>");
            
            out.println("<div style='margin-top: 30px;'>");
            out.println("<a href='ShowMaintenance' class='btn'>Back to Dashboard</a>");
            out.println("<a href='ShowVehicleById?id=" + task.getVehicleId() + "' class='btn'>View Vehicle</a>");
            out.println(" </div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowMaintenance");
        }
         
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

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
