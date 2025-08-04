package MaintenanceLogDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import vehicelDAO.DataSource;

/**
 *
 * @author Chester
 */
public class MaintenanceLogImpl implements MaintenanceLogDAO {
    private static final String INSERT_MAINTENANCE_LOG = 
        "INSERT INTO MaintenanceLog (vehicle_id, task_description, scheduled_date, completion_date, status) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_ALL_MAINTENANCE_LOGS = 
        "SELECT ml.*, v.vehicle_number FROM maintenancelog ml " +
        "JOIN vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "ORDER BY ml.scheduled_date DESC, ml.task_id DESC";
    
    private static final String SELECT_BY_VEHICLE_ID = 
        "SELECT ml.*, v.vehicle_number FROM MaintenanceLog ml " +
        "JOIN Vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "WHERE ml.vehicle_id = ? ORDER BY ml.scheduled_date DESC";
    
    private static final String SELECT_BY_STATUS = 
        "SELECT ml.*, v.vehicle_number FROM MaintenanceLog ml " +
        "JOIN Vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "WHERE ml.status = ? ORDER BY ml.scheduled_date DESC";
    
    private static final String SELECT_BY_ID = 
        "SELECT ml.*, v.vehicle_number FROM MaintenanceLog ml " +
        "JOIN Vehicles v ON ml.vehicle_id = v.vehicle_id " +
        "WHERE ml.task_id = ?";
    
    private static final String UPDATE_MAINTENANCE_LOG = 
        "UPDATE maintenancelog SET vehicle_id = ?, task_description = ?, scheduled_date = ?, completion_date = ?, status = ? WHERE task_id = ?";
    
    private static final String DELETE_MAINTENANCE_LOG = 
        "DELETE FROM maintenancelog WHERE task_id = ?";
    
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
            
            if (rowsAffected > 0) {
                return true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
