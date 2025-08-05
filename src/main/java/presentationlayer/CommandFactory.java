package presentationlayer;

import businesslayers.command.ReportAndGpsCommand.LogOperatorStatusCommand;
import businesslayers.command.userAndLoginCommand.NavigateToRegisterPageCommand;
import businesslayers.command.Command;
import businesslayers.command.maintenanceCommand.MaintenanceDashboardCommand;
import businesslayers.command.userAndLoginCommand.LoginCommand;
import businesslayers.command.ReportAndGpsCommand.SimulateGPSCommand;
import businesslayers.command.ReportAndGpsCommand.GenerateReportCommand;
import businesslayers.command.vehiclecommand.RegisterVehicleCommand;
import businesslayers.command.userAndLoginCommand.RegisterUserCommand;
import businesslayers.command.userAndLoginCommand.LogoutCommand;
import businesslayers.command.ReportAndGpsCommand.RunSystemChecksCommand;
import businesslayers.command.maintenanceCommand.ShowMaintenanceByIdCommand;
import businesslayers.command.maintenanceCommand.ShowMaintenanceCommand;
import businesslayers.command.vehiclecommand.ShowVehicleByIdCommand;
import businesslayers.command.vehiclecommand.ShowVehicleListCommand;
import businesslayers.command.vehiclecommand.UpdateVehicleCommand;

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
        commands.put("updateVehicle", new UpdateVehicleCommand());
        commands.put("showVehicleList", new ShowVehicleListCommand());
        commands.put("showVehicleById", new ShowVehicleByIdCommand());
        commands.put("showMaintenance", new ShowMaintenanceCommand());
        commands.put("showMaintenanceById", new ShowMaintenanceByIdCommand());
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
