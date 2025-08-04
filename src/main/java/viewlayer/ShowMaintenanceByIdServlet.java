package viewlayer;

import MaintenanceLogDAO.MaintenanceLog;
import MaintenanceLogDAO.MaintenanceLogLogic;
import userDAO.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ShowMaintenanceByIdServlet - Shows maintenance task details
 * Accessible by all authenticated users (Managers and Operators)
 * @author Chester
 */
public class ShowMaintenanceByIdServlet extends HttpServlet {
    private MaintenanceLogLogic logic = new MaintenanceLogLogic();

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
        
        String taskIdStr = request.getParameter("id");
        
        try {
            int taskId = Integer.parseInt(taskIdStr);
            MaintenanceLog task = logic.getMaintenanceLogById(taskId);

            if (task == null) {
                response.sendRedirect("ShowMaintenance?error=notfound");
                return;
            }

            // Set attributes for JSP
            request.setAttribute("task", task);
            request.setAttribute("isManager", isManager(request));

            // Forward to JSP
            request.getRequestDispatcher("/views/maintenance-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowMaintenance?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load maintenance details");
            response.sendRedirect("ShowMaintenance?error=system");
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
        return "Maintenance Details Servlet - Authenticated Access";
    }
}