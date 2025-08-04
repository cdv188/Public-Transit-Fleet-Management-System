package viewlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vehicelDAO.VehicleDAO;
import vehicelDAO.VehicleLogic;
import vehicleSimpleFactory.Vehicle;

/**
 *
 * @author Chester
 */
public class ShowVehicleListServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String successMessage = (String) request.getSession().getAttribute("successMessage");
        VehicleLogic logic = new VehicleLogic();
        List<Vehicle> vehicles = logic.getAllVehicles();
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Vehicle List - PTFMS</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<link rel=\"stylesheet\" href=\"vehicle.css\">  ");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<div class='nav'>");
        out.println("<a href='RegisterVehicle'>Register Vehicle</a>");
        out.println("<a href='ShowMaintenance'>Maintenance Dashboard</a>");
        out.println("</div>");
        out.println("<h1>Vehicle Fleet Management</h1>");
        
        if (successMessage != null) {
            out.println("<div class='success'>" + successMessage + "</div>");
            request.getSession().removeAttribute("successMessage");
        }
        
        out.println("<div style='margin-bottom: 20px;'>");
        out.println("<a href='RegisterVehicle' class='btn btn-success'>Add New Vehicle</a>");
        out.println("</div>");
        
        if (vehicles.isEmpty()) {
            out.println("<p>No vehicles registered yet. <a href='RegisterVehicle'>Register the first vehicle</a></p>");
        } else {
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Vehicle Number</th>");
            out.println("<th>Type</th>");
            out.println("<th>Fuel Type</th>");
            out.println("<th>Consumption Rate</th>");
            out.println("<th>Max Passengers</th>");
            out.println("<th>Assigned Route</th>");
            out.println("<th>Actions</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            
            for (Vehicle vehicle : vehicles) {
                out.println("<tr>");
                out.println("<td>" + vehicle.getNumber() + "</td>");
                out.println("<td>" + vehicle.getVehicleType() + "</td>");
                out.println("<td>" + vehicle.getFuelType() + "</td>");
                out.println("<td>" + vehicle.getConsumptionRate() + "</td>");
                out.println("<td>" + vehicle.getMaxCapacity() + "</td>");
                out.println("<td>" + vehicle.getRoute() + "</td>");
                out.println("<td>");
                out.println("<a href='ShowVehicleById?id=" + vehicle.getVehicleId() + "' class='btn'>View Details</a>");
                out.println("</td>");
                out.println("</tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            out.println("<p><strong>Total Vehicles:</strong> " + vehicles.size() + "</p>");
        }
        
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
