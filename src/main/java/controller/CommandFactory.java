package controller;

import businesslayers.command.LogOperatorStatusCommand; // Import from the new package
import businesslayers.command.NavigateToRegisterPageCommand;
import businesslayers.command.Command;
import businesslayers.command.MaintenanceDashboardCommand;
import businesslayers.command.LoginCommand;
import businesslayers.command.SimulateGPSCommand;
import businesslayers.command.GenerateReportCommand;
import businesslayers.command.RegisterVehicleCommand;
import businesslayers.command.RegisterUserCommand;
import businesslayers.command.LogoutCommand;
import businesslayers.command.RunSystemChecksCommand;
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
        
        // Ali's commands
        commands.put("runSystemChecks", new RunSystemChecksCommand());
        commands.put("logOperatorStatus", new LogOperatorStatusCommand());
        commands.put("simulateGPS", new SimulateGPSCommand());
        commands.put("generateReport", new GenerateReportCommand());
        
        // Chester's commands
        commands.put("maintenanceDashboard", new MaintenanceDashboardCommand());
        commands.put("registerVehicle", new RegisterVehicleCommand());
    }

    public static Command getCommand(String action) {
        // The default command now directly handles the response.
        return commands.getOrDefault(action, (request, response) -> {
            // Redirect to the index page for any unknown actions.
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        });
    }
}