package controller;

import businesslayers.command.Command;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Unified FrontController that handles all command routing.
 * Uses CommandFactory for all commands and supports special routing needs.
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
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        // Handle null or empty action
        if (action == null || action.isEmpty()) {
            action = "default";
        }
        
        try {
            // Handle special cases that don't use commands
            if ("viewGpsDashboard".equals(action)) {
                request.getRequestDispatcher("/gpsDashboard").forward(request, response);
                return;
            }
            
            // Get the command from the factory
            Command command = CommandFactory.getCommand(action);
            
            // Execute the command
            command.execute(request, response);
            
            // Check if this command also has the executeWithResult method
            // This handles commands that may return a String for forwarding
            try {
                String nextPage = (String) command.getClass()
                        .getMethod("executeWithResult", HttpServletRequest.class, HttpServletResponse.class)
                        .invoke(command, request, response);
                
                if (nextPage != null) {
                    request.getRequestDispatcher(nextPage).forward(request, response);
                }
            } catch (NoSuchMethodException ignored) {
                // Command doesn't have executeWithResult method, which is fine
                // The command.execute() method should have handled everything
            } catch (Exception e) {
                throw new ServletException("Error executing executeWithResult for action: " + action, e);
            }
            
        } catch (Exception e) {
            throw new ServletException("Error executing command for action: " + action, e);
        }
    }
}