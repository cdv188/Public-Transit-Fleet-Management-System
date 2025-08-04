package controller;

import command.*;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        // Louis's commands
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("register", new RegisterUserCommand());
        commands.put("navigateToRegister", new NavigateToRegisterPageCommand());
        
        // Ali's commands
        commands.put("runSystemChecks", new RunSystemChecksCommand());
        commands.put("logOperatorStatus", new LogOperatorStatusCommand());
        commands.put("simulateGPS", new SimulateGPSCommand());
        commands.put("generateReport", new GenerateReportCommand());
    }

    public static Command getCommand(String action) {
        return commands.getOrDefault(action, (request, response) -> {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        });
    }
}