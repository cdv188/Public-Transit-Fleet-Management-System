package viewlayer;

import command.Command;
import command.RunSystemChecksCommand;
import command.LogOperatorStatusCommand;
import command.SimulateGPSCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Minimal FrontController to test Command Pattern integration and GPS dashboard.
 * Temporary version for development until Louis's final version is done.
 * 
 * Updated to handle both Chester's void commands and Ali's commands that may return a String.
 * 
 * @author Ali
 */
public class FrontController extends HttpServlet {

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

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Command command = null;

        try {
            if (action != null) {
                switch (action) {
                    case "runSystemChecks":
                        command = new RunSystemChecksCommand();
                        break;
                    case "logOperatorStatus":
                        command = new LogOperatorStatusCommand();
                        break;
                    case "simulateGPS":
                        command = new SimulateGPSCommand();
                        break;
                    case "viewGpsDashboard":
                        request.getRequestDispatcher("/gpsDashboard").forward(request, response);
                        return;
                    default:
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action: " + action);
                        return;
                }

                if (command != null) {
                    
                    command.execute(request, response);

                    // If this command also has a result method, call it
                    try {
                        String nextPage = (String) command.getClass()
                                .getMethod("executeWithResult", HttpServletRequest.class, HttpServletResponse.class)
                                .invoke(command, request, response);

                        if (nextPage != null) {
                            request.getRequestDispatcher(nextPage).forward(request, response);
                        }
                    } catch (NoSuchMethodException ignored) {
                        // Command doesn't have executeWithResult, so ignore
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
            }
        } catch (Exception e) {
            throw new ServletException("Error executing command for action: " + action, e);
        }
    }
}
