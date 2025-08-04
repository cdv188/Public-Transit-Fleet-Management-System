/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Strategy interface for generating different types of reports
 * @author Ali
 */
public interface ReportStrategy {
    /**
     * Generate a report based on the request parameters
     * @param request The HTTP request
     * @param response The HTTP response
     * @return Map containing report data
     */
    Map<String, Object> generate(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * Get the report title
     * @return The title of the report
     */
    String getReportTitle();
}