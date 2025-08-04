package command;

import dao.OperatorStatusLogsDAO;
import dao.OperatorStatusLogsDAOImpl;
import dto.OperatorStatusLog;
import dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * Command for operators to log their status
 * @author Ali
 */
public class LogOperatorStatusCommand implements Command {

    /**
     * Required by Chester's Command interface (void return type).
     * This simply calls the original logic.
     */
   // same package/imports as before

@Override
public void execute(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    executeWithResult(request, response);
}

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

            if (saved) {
                showStatusPage(out, request, currentUser, "Status updated successfully!", true);
            } else {
                showStatusPage(out, request, currentUser, "Failed to update status.", false);
            }
        } else {
            showStatusPage(out, request, currentUser, null, false);
        }
        return null;
    } catch (Exception e) {
        e.printStackTrace();
        return "/error.html";
    }
}

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
    out.println("<link rel='stylesheet' href='commands.css'>"); // CSS 
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