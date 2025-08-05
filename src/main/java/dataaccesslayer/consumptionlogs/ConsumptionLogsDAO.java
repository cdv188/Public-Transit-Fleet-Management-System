package dataaccesslayer.consumptionlogs;

import datatransferobject.ConsumptionLog;
import java.sql.Date;
import java.util.List;

/**
 * DAO interface for vehicle fuel consumption logs.
 */
public interface ConsumptionLogsDAO {

    /**
     * Adds a new consumption log.
     *
     * @param log the log to add
     * @return true if successful
     */
    boolean addConsumptionLog(ConsumptionLog log);

    /**
     * Retrieves all consumption logs for a vehicle.
     *
     * @param vehicleId the vehicle ID
     * @return list of logs
     */
    List<ConsumptionLog> getConsumptionByVehicle(int vehicleId);

    /**
     * Retrieves logs for a vehicle within a date range.
     *
     * @param vehicleId the vehicle ID
     * @param startDate start date
     * @param endDate   end date
     * @return list of logs
     */
    List<ConsumptionLog> getConsumptionByDateRange(int vehicleId, Date startDate, Date endDate);

    /**
     * Retrieves the average consumption for a vehicle.
     *
     * @param vehicleId the vehicle ID
     * @return average consumption
     */
    double getAverageConsumption(int vehicleId);
}
