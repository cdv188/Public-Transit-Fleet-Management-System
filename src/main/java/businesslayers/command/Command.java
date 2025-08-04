package businesslayers.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Executes a command, handling the request and response directly.
 * The command is responsible for forwarding or redirecting as needed.
 */
public interface Command {
     /**
     * Executes the command with the given request and response.
     * @param request The HTTP servlet request.
     * @param response The HTTP servlet response.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException if an I/O error occurs.
     */
    void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}