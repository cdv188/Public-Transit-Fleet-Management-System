package controller;

import command.*; // Import from the new package
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandFactory {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("register", new RegisterUserCommand());
        commands.put("navigateToRegister", new NavigateToRegisterPageCommand());
    }

    public static Command getCommand(String action) {
        // The default command now directly handles the response.
        return commands.getOrDefault(action, (request, response) -> {
            // Redirect to the index page for any unknown actions.
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        });
    }
}