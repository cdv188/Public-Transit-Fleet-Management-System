/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewlayer;

import command.SimulateGPSCommand;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for GPS Simulation
 * @author Ali
 */
public class SimulateGPSServlet extends HttpServlet {
    
    private SimulateGPSCommand simulateCommand;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.simulateCommand = new SimulateGPSCommand();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        simulateCommand.execute(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        simulateCommand.execute(request, response);
    }
}