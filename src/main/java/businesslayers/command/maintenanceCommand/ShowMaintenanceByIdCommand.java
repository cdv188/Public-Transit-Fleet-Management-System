package businesslayers.command.maintenanceCommand;

import businesslayers.command.Command;
import dataaccesslayer.maintenance.MaintenanceLog;
import dataaccesslayer.maintenance.MaintenanceLogLogic;
import dataaccesslayer.users.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Command for showing details of a specific maintenance task.
 */
public class ShowMaintenanceByIdCommand implements Command {

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

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
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
                response.sendRedirect("FrontController?action=showMaintenance&error=notfound");
                return;
            }

            request.setAttribute("task", task);
            request.setAttribute("isManager", isManager(request));
            request.getRequestDispatcher("/views/maintenance-details.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("FrontController?action=showMaintenance&error=invalid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("FrontController?action=showMaintenance&error=system");
        }
    }
}