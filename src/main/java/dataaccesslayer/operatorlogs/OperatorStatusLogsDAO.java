package dataaccesslayer.operatorlogs;

import datatransferobject.OperatorStatusLog;
import java.util.List;

/**
 * DAO interface for operator status log operations.
 */
public interface OperatorStatusLogsDAO {

    /**
     * Adds a new operator status log.
     *
     * @param log the status log to add
     * @return true if successful
     */
    boolean addStatusLog(OperatorStatusLog log);

    /**
     * Retrieves all status logs for an operator.
     *
     * @param userId the operator's user ID
     * @return list of status logs
     */
    List<OperatorStatusLog> getLogsByOperator(int userId);

    /**
     * Retrieves the most recent status for an operator.
     *
     * @param userId the operator's user ID
     * @return the most recent status log, or null if none
     */
    OperatorStatusLog getCurrentStatus(int userId);

    /**
     * Retrieves all operators with a specific status.
     *
     * @param status the status to search for
     * @return list of status logs with that status
     */
    List<OperatorStatusLog> getLogsByStatus(String status);
}
