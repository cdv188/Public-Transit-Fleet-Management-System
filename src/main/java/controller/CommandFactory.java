package controller;

import businesslayers.command.LogOperatorStatusCommand;
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

/**
 * Factory that maps action strings to {@link Command} instances.
 */
public class CommandFactory {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("register", new RegisterUserCommand());
        commands.put("navigateToRegister", new NavigateToRegisterPageCommand());

        commands.put("runSystemChecks", new RunSystemChecksCommand());
        commands.put("logOperatorStatus", new LogOperatorStatusCommand());
        commands.put("simulateGPS", new SimulateGPSCommand());
        commands.put("generateReport", new GenerateReportCommand());

        commands.put("maintenanceDashboard", new MaintenanceDashboardCommand());
        commands.put("registerVehicle", new RegisterVehicleCommand());
    }

    /**
     * Returns the {@link Command} associated with the given action.
     * If no command matches, returns a default command that redirects to the home page.
     *
     * @param action the action name
     * @return the corresponding command
     */
    public static Command getCommand(String action) {
        return commands.getOrDefault(action, (request, response) -> {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        });
    }
}
