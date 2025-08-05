package businesslayers.command.ReportAndGpsCommand;

import businesslayers.command.Command;
import dataaccesslayer.operatorlogs.OperatorStatusLogsDAO;
import dataaccesslayer.operatorlogs.OperatorStatusLogsDAOImpl;
import datatransferobject.OperatorStatusLog;
import dataaccesslayer.users.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * Command that allows operators to log and update their status.
 */
public class LogOperatorStatusCommand implements Command {

    /**
     * Executes the operator status logging command.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        executeWithResult(request, response);
    }

    /**
     * Executes the command and returns a result string if needed.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return an optional result string, or {@code null} if handled directly
     */
    public String executeWithResult(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null) {
                response.sendRedirect(request.getContextPath() + "/login.html");
                return null;
            }
            if (!"Operator".equals(currentUser.getUserType())) {
                response.sendRedirect(request.getContextPath() + "/error.html");
                return null;
            }

            String newStatus = request.getParameter("status");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            if (newStatus != null && !newStatus.isEmpty()) {
                OperatorStatusLogsDAO statusDAO = new OperatorStatusLogsDAOImpl();
                OperatorStatusLog log = new OperatorStatusLog();
                log.setUserId(currentUser.getUserId());
                log.setStatus(newStatus);
                log.setTimestamp(new Timestamp(System.currentTimeMillis()));

                boolean saved = statusDAO.addStatusLog(log);
                showStatusPage(out, request, currentUser,
                        saved ? "Status updated successfully!" : "Failed to update status.", saved);
            } else {
                showStatusPage(out, request, currentUser, null, false);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return "/error.html";
        }
    }

    /**
     * Displays the operator status update page.
     *
     * @param out       the writer for the HTTP response
     * @param request   the HTTP request
     * @param user      the current operator
     * @param message   an optional message to display
     * @param isSuccess whether the operation was successful
     */
    private void showStatusPage(PrintWriter out, HttpServletRequest request, User user,
                                String message, boolean isSuccess) {
        OperatorStatusLogsDAO statusDAO = new OperatorStatusLogsDAOImpl();
        OperatorStatusLog currentStatus = statusDAO.getCurrentStatus(user.getUserId());

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Operator Status - PTFMS</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<link rel='stylesheet' href='css/commands.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");

        out.println("<h1>Operator Status Update</h1>");
        out.println("<p>Welcome, " + user.getName() + "</p>");

        if (currentStatus != null) {
            out.println("<div class='current-status'>");
            out.println("<h3>Current Status</h3>");
            out.println("<p><strong>Status:</strong> " + currentStatus.getStatus() + "</p>");
            out.println("<p><strong>Since:</strong> " + currentStatus.getTimestamp() + "</p>");
            out.println("</div>");
        }

        if (message != null) {
            out.println("<p class='" + (isSuccess ? "success" : "error") + "'>" + message + "</p>");
        }

        out.println("<div class='status-form'>");
        out.println("<h3>Update Status</h3>");
        out.println("<form method='post'>");
        out.println("<div class='form-group'>");
        out.println("<label for='status'>New Status:</label>");
        out.println("<select id='status' name='status' required>");
        out.println("<option value=''>Select Status</option>");
        out.println("<option value='In-Service'>In-Service</option>");
        out.println("<option value='On Break'>On Break</option>");
        out.println("<option value='Out-of-Service'>Out-of-Service</option>");
        out.println("</select>");
        out.println("</div>");
        out.println("<button type='submit' class='btn'>Update Status</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
