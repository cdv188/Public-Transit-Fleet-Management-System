package controller;

import businesslayers.command.Command;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Front controller servlet that routes all requests to the appropriate command.
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

    /**
     * Processes requests by determining the action, retrieving the corresponding command,
     * and executing it.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "default";
        }

        try {
            if ("viewGpsDashboard".equals(action)) {
                request.getRequestDispatcher("/gpsDashboard").forward(request, response);
                return;
            }

            Command command = CommandFactory.getCommand(action);
            command.execute(request, response);

            try {
                String nextPage = (String) command.getClass()
                        .getMethod("executeWithResult", HttpServletRequest.class, HttpServletResponse.class)
                        .invoke(command, request, response);

                if (nextPage != null) {
                    request.getRequestDispatcher(nextPage).forward(request, response);
                }
            } catch (NoSuchMethodException ignored) {
                // Command does not implement executeWithResult
            } catch (Exception e) {
                throw new ServletException("Error executing executeWithResult for action: " + action, e);
            }

        } catch (Exception e) {
            throw new ServletException("Error executing command for action: " + action, e);
        }
    }
}
