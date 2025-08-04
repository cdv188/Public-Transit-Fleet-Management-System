/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.OperatorStatusLog;
import vehicelDAO.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of OperatorStatusLogsDAO
 * @author Ali
 */
public class OperatorStatusLogsDAOImpl implements OperatorStatusLogsDAO {
    
    private static final String INSERT_LOG = 
        "INSERT INTO OperatorStatusLogs (user_id, status, timestamp) VALUES (?, ?, ?)";
    
    private static final String GET_BY_OPERATOR = 
        "SELECT * FROM OperatorStatusLogs WHERE user_id = ? ORDER BY timestamp DESC";
    
    private static final String GET_CURRENT_STATUS = 
        "SELECT * FROM OperatorStatusLogs WHERE user_id = ? ORDER BY timestamp DESC LIMIT 1";
    
    private static final String GET_BY_STATUS = 
        "SELECT * FROM OperatorStatusLogs WHERE status = ? ORDER BY timestamp DESC";
    
    @Override
    public boolean addStatusLog(OperatorStatusLog log) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_LOG)) {
            
            pstmt.setInt(1, log.getUserId());
            pstmt.setString(2, log.getStatus());
            pstmt.setTimestamp(3, log.getTimestamp());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<OperatorStatusLog> getLogsByOperator(int userId) {
        List<OperatorStatusLog> logs = new ArrayList<>();
        
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BY_OPERATOR)) {
            
            pstmt.setInt(1, userId);
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
    public OperatorStatusLog getCurrentStatus(int userId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_CURRENT_STATUS)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractLogFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<OperatorStatusLog> getLogsByStatus(String status) {
        List<OperatorStatusLog> logs = new ArrayList<>();
        
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_BY_STATUS)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(extractLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return logs;
    }
    
    private OperatorStatusLog extractLogFromResultSet(ResultSet rs) throws SQLException {
        OperatorStatusLog log = new OperatorStatusLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getInt("user_id"));
        log.setStatus(rs.getString("status"));
        log.setTimestamp(rs.getTimestamp("timestamp"));
        return log;
    }
}