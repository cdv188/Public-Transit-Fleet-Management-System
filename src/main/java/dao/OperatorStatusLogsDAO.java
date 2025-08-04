/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.OperatorStatusLog;
import java.util.List;

/**
 * DAO interface for OperatorStatusLogs operations
 * @author Ali
 */
public interface OperatorStatusLogsDAO {
    /**
     * Add a new operator status log
     * @param log The status log to add
     * @return true if successful
     */
    boolean addStatusLog(OperatorStatusLog log);
    
    /**
     * Get all status logs for an operator
     * @param userId The user ID of the operator
     * @return List of status logs
     */
    List<OperatorStatusLog> getLogsByOperator(int userId);
    
    /**
     * Get the most recent status for an operator
     * @param userId The user ID of the operator
     * @return The most recent status log or null
     */
    OperatorStatusLog getCurrentStatus(int userId);
    
    /**
     * Get all operators with a specific status
     * @param status The status to search for
     * @return List of status logs with that status
     */
    List<OperatorStatusLog> getLogsByStatus(String status);
}