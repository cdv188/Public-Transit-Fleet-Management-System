package businesslayers.strategy;

import dataaccesslayer.operatorlogs.OperatorStatusLogsDAO;
import dataaccesslayer.operatorlogs.OperatorStatusLogsDAOImpl;
import datatransferobject.OperatorStatusLog;
import dataaccesslayer.users.UserDAO;
import dataaccesslayer.users.UserDAOImpl;
import dataaccesslayer.users.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Strategy that generates an operator performance analysis report.
 */
public class OperatorPerformanceReportStrategy implements ReportStrategy {

    /**
     * Generates performance statistics for all operators.
     */
    @Override
    public Map<String, Object> generate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> reportData = new HashMap<>();

        UserDAO userDAO = new UserDAOImpl();
        OperatorStatusLogsDAO logsDAO = new OperatorStatusLogsDAOImpl();

        List<User> operators = userDAO.getUsersByType("Operator");
        List<Map<String, Object>> operatorStats = new ArrayList<>();

        for (User operator : operators) {
            List<OperatorStatusLog> logs = logsDAO.getLogsByOperator(operator.getUserId());

            int inServiceCount = 0;
            int onBreakCount = 0;
            int outOfServiceCount = 0;

            for (OperatorStatusLog log : logs) {
                switch (log.getStatus()) {
                    case "In-Service": inServiceCount++; break;
                    case "On Break": onBreakCount++; break;
                    case "Out-of-Service": outOfServiceCount++; break;
                }
            }

            int totalLogs = logs.size();
            double serviceRate = totalLogs > 0 ? (double) inServiceCount / totalLogs * 100 : 0.0;

            Map<String, Object> operatorData = new HashMap<>();
            operatorData.put("operatorId", operator.getUserId());
            operatorData.put("operatorName", operator.getName());
            operatorData.put("operatorEmail", operator.getEmail());
            operatorData.put("totalLogs", totalLogs);
            operatorData.put("inServiceCount", inServiceCount);
            operatorData.put("onBreakCount", onBreakCount);
            operatorData.put("outOfServiceCount", outOfServiceCount);
            operatorData.put("serviceRate", serviceRate);

            operatorStats.add(operatorData);
        }

        operatorStats.sort((a, b) ->
            Double.compare((Double) b.get("serviceRate"), (Double) a.get("serviceRate")));

        reportData.put("operatorStats", operatorStats);
        reportData.put("totalOperators", operators.size());
        reportData.put("reportDate", new Date());

        return reportData;
    }

    /** @return the report title. */
    @Override
    public String getReportTitle() {
        return "Operator Performance Analysis Report";
    }
}
