package viewlayer;

import dataaccesslayer.maintenance.MaintenanceLog;
import dataaccesslayer.maintenance.MaintenanceLogLogic;
import dataaccesslayer.users.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet for showing details of a specific maintenance task.
 */
public class ShowMaintenanceByIdServlet extends HttpServlet {

    private MaintenanceLogLogic logic = new MaintenanceLogLogic();

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

    /** Processes both GET and POST requests for showing maintenance details. */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

            request.setAttribute("task", task);
            request.setAttribute("isManager", isManager(request));
            request.getRequestDispatcher("/views/maintenance-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("ShowMaintenance?error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
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
        return "Shows details of a maintenance task (authenticated access).";
    }
}
