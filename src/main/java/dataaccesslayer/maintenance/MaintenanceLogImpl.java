package dataaccesslayer.maintenance;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import database.DataSource;

/**
 * Implementation of the {@link MaintenanceLogDAO} interface for managing
 * maintenance log records in the database. This class handles CRUD operations
 * for {@link MaintenanceLog} objects, including scheduling tasks, retrieving
 * records, updating entries, and deleting logs.
 */
public class MaintenanceLogImpl implements MaintenanceLogDAO {

    /** SQL query for inserting a new maintenance log record. */
    private static final String INSERT_MAINTENANCE_LOG =
        "INSERT INTO MaintenanceLog (vehicle_id, task_description, scheduled_date, completion_date, status) VALUES (?, ?, ?, ?, ?)";

    /** SQL query for selecting all maintenance logs with vehicle numbers. */
    private static final String SELECT_ALL_MAINTENANCE_LOGS =
        "SELECT ml.*, v.vehicle_number FROM maintenancelog ml " +
        "JOIN vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "ORDER BY ml.scheduled_date DESC, ml.task_id DESC";

    /** SQL query for selecting maintenance logs by vehicle ID. */
    private static final String SELECT_BY_VEHICLE_ID =
        "SELECT ml.*, v.vehicle_number FROM MaintenanceLog ml " +
        "JOIN Vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "WHERE ml.vehicle_id = ? ORDER BY ml.scheduled_date DESC";

    /** SQL query for selecting maintenance logs by status. */
    private static final String SELECT_BY_STATUS =
        "SELECT ml.*, v.vehicle_number FROM MaintenanceLog ml " +
        "JOIN Vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "WHERE ml.status = ? ORDER BY ml.scheduled_date DESC";

    /** SQL query for selecting a maintenance log by task ID. */
    private static final String SELECT_BY_ID =
        "SELECT ml.*, v.vehicle_number FROM MaintenanceLog ml " +
        "JOIN Vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "WHERE ml.task_id = ?";

    /** SQL query for updating a maintenance log record. */
    private static final String UPDATE_MAINTENANCE_LOG =
        "UPDATE maintenancelog SET vehicle_id = ?, task_description = ?, scheduled_date = ?, completion_date = ?, status = ? WHERE task_id = ?";

    /** SQL query for deleting a maintenance log record by task ID. */
    private static final String DELETE_MAINTENANCE_LOG =
        "DELETE FROM maintenancelog WHERE task_id = ?";

    /**
     * Schedules a new maintenance task in the database.
     *
     * @param task the {@link MaintenanceLog} object containing task details
     * @return {@code true} if the task was successfully inserted, {@code false} otherwise
     */
    @Override
    public boolean scheduleNewTask(MaintenanceLog task) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MAINTENANCE_LOG)) {

            stmt.setInt(1, task.getVehicleId());
            stmt.setString(2, task.getTaskDescription());

            if (task.getScheduledDate() != null) {
                stmt.setDate(3, Date.valueOf(task.getScheduledDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (task.getCompletionDate() != null) {
                stmt.setDate(4, Date.valueOf(task.getCompletionDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, task.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all maintenance logs along with their associated vehicle numbers.
     *
     * @return a list of {@link MaintenanceLog} objects
     */
    @Override
    public List<MaintenanceLog> getAlarmsAndTasks() {
        List<MaintenanceLog> maintenanceLogs = new ArrayList<>();

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_MAINTENANCE_LOGS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MaintenanceLog log = new MaintenanceLog();
                log.setTaskId(rs.getInt("task_id"));
                log.setVehicleId(rs.getInt("vehicle_id"));
                log.setTaskDescription(rs.getString("task_description"));

                Date scheduledDate = rs.getDate("scheduled_date");
                if (scheduledDate != null) {
                    log.setScheduledDate(scheduledDate.toLocalDate());
                }

                Date completionDate = rs.getDate("completion_date");
                if (completionDate != null) {
                    log.setCompletionDate(completionDate.toLocalDate());
                }

                log.setStatus(rs.getString("status"));
                log.setVehicleNumber(rs.getString("vehicle_number"));

                maintenanceLogs.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maintenanceLogs;
    }

    /**
     * Retrieves maintenance logs for a specific vehicle.
     *
     * @param vehicleId the ID of the vehicle
     * @return a list of {@link MaintenanceLog} objects associated with the given vehicle ID
     */
    @Override
    public List<MaintenanceLog> getMaintenanceLogsByVehicleId(int vehicleId) {
        List<MaintenanceLog> maintenanceLogs = new ArrayList<>();

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_VEHICLE_ID)) {

            stmt.setInt(1, vehicleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaintenanceLog log = new MaintenanceLog();
                    log.setTaskId(rs.getInt("task_id"));
                    log.setVehicleId(rs.getInt("vehicle_id"));
                    log.setTaskDescription(rs.getString("task_description"));

                    Date scheduledDate = rs.getDate("scheduled_date");
                    if (scheduledDate != null) {
                        log.setScheduledDate(scheduledDate.toLocalDate());
                    }

                    Date completionDate = rs.getDate("completion_date");
                    if (completionDate != null) {
                        log.setCompletionDate(completionDate.toLocalDate());
                    }

                    log.setStatus(rs.getString("status"));
                    log.setVehicleNumber(rs.getString("vehicle_number"));

                    maintenanceLogs.add(log);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maintenanceLogs;
    }

    /**
     * Retrieves maintenance logs filtered by status.
     *
     * @param status the status to filter by (e.g., "Scheduled", "Completed")
     * @return a list of {@link MaintenanceLog} objects matching the given status
     */
    @Override
    public List<MaintenanceLog> getMaintenanceLogsByStatus(String status) {
        List<MaintenanceLog> maintenanceLogs = new ArrayList<>();

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_STATUS)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MaintenanceLog log = new MaintenanceLog();
                    log.setTaskId(rs.getInt("task_id"));
                    log.setVehicleId(rs.getInt("vehicle_id"));
                    log.setTaskDescription(rs.getString("task_description"));

                    Date scheduledDate = rs.getDate("scheduled_date");
                    if (scheduledDate != null) {
                        log.setScheduledDate(scheduledDate.toLocalDate());
                    }

                    Date completionDate = rs.getDate("completion_date");
                    if (completionDate != null) {
                        log.setCompletionDate(completionDate.toLocalDate());
                    }

                    log.setStatus(rs.getString("status"));
                    log.setVehicleNumber(rs.getString("vehicle_number"));

                    maintenanceLogs.add(log);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maintenanceLogs;
    }

    /**
     * Updates an existing maintenance log record in the database.
     *
     * @param maintenanceLog the {@link MaintenanceLog} object containing updated details
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    @Override
    public boolean updateMaintenanceLog(MaintenanceLog maintenanceLog) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_MAINTENANCE_LOG)) {

            stmt.setInt(1, maintenanceLog.getVehicleId());
            stmt.setString(2, maintenanceLog.getTaskDescription());

            if (maintenanceLog.getScheduledDate() != null) {
                stmt.setDate(3, Date.valueOf(maintenanceLog.getScheduledDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (maintenanceLog.getCompletionDate() != null) {
                stmt.setDate(4, Date.valueOf(maintenanceLog.getCompletionDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, maintenanceLog.getStatus());
            stmt.setInt(6, maintenanceLog.getTaskId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a maintenance log by its task ID.
     *
     * @param taskId the ID of the maintenance task
     * @return the corresponding {@link MaintenanceLog} object, or {@code null} if not found
     */
    @Override
    public MaintenanceLog getMaintenanceLogById(int taskId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, taskId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MaintenanceLog log = new MaintenanceLog();
                    log.setTaskId(rs.getInt("task_id"));
                    log.setVehicleId(rs.getInt("vehicle_id"));
                    log.setTaskDescription(rs.getString("task_description"));

                    Date scheduledDate = rs.getDate("scheduled_date");
                    if (scheduledDate != null) {
                        log.setScheduledDate(scheduledDate.toLocalDate());
                    }

                    Date completionDate = rs.getDate("completion_date");
                    if (completionDate != null) {
                        log.setCompletionDate(completionDate.toLocalDate());
                    }

                    log.setStatus(rs.getString("status"));
                    log.setVehicleNumber(rs.getString("vehicle_number"));

                    return log;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes a maintenance log record by task ID.
     *
     * @param taskId the ID of the maintenance task to delete
     * @return {@code true} if the deletion was successful, {@code false} otherwise
     */
    @Override
    public boolean deleteMaintenanceLog(int taskId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_MAINTENANCE_LOG)) {

            stmt.setInt(1, taskId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
