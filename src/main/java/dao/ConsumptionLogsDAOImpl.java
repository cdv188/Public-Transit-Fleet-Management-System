/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.ConsumptionLog;
import vehicelDAO.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ConsumptionLogsDAO
 * @author Ali
 */
public class ConsumptionLogsDAOImpl implements ConsumptionLogsDAO {
    
    private static final String INSERT_LOG = 
        "INSERT INTO ConsumptionLogs (vehicle_id, log_date, usage_amount) VALUES (?, ?, ?)";
    
    private static final String GET_BY_VEHICLE = 
        "SELECT * FROM ConsumptionLogs WHERE vehicle_id = ? ORDER BY log_date DESC";
    
    private static final String GET_BY_DATE_RANGE = 
        "SELECT * FROM ConsumptionLogs WHERE vehicle_id = ? AND log_date BETWEEN ? AND ? ORDER BY log_date";
    
    private static final String GET_AVERAGE_CONSUMPTION = 
        "SELECT AVG(usage_amount) as avg_usage FROM ConsumptionLogs WHERE vehicle_id = ?";
    
    @Override
    public boolean addConsumptionLog(ConsumptionLog log) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_LOG)) {
            
            pstmt.setInt(1, log.getVehicleId());
            pstmt.setDate(2, log.getLogDate());
            pstmt.setDouble(3, log.getUsageAmount());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<ConsumptionLog> getConsumptionByVehicle(int vehicleId) {
        List<ConsumptionLog> logs = new ArrayList<>();
        
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BY_VEHICLE)) {
            
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(extractLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return logs;
    }
    
    @Override
    public List<ConsumptionLog> getConsumptionByDateRange(int vehicleId, Date startDate, Date endDate) {
        List<ConsumptionLog> logs = new ArrayList<>();
        
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BY_DATE_RANGE)) {
            
            pstmt.setInt(1, vehicleId);
            pstmt.setDate(2, startDate);
            pstmt.setDate(3, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(extractLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return logs;
    }
    
    @Override
    public double getAverageConsumption(int vehicleId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_AVERAGE_CONSUMPTION)) {
            
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_usage");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }
    
    private ConsumptionLog extractLogFromResultSet(ResultSet rs) throws SQLException {
        ConsumptionLog log = new ConsumptionLog();
        log.setLogId(rs.getInt("log_id"));
        log.setVehicleId(rs.getInt("vehicle_id"));
        log.setLogDate(rs.getDate("log_date"));
        log.setUsageAmount(rs.getDouble("usage_amount"));
        return log;
    }
}