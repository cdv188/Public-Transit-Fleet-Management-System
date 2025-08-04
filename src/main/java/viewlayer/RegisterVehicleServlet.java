package viewlayer;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vehicelDAO.VehicleLogic;
import vehicleSimpleFactory.Vehicle;

/**
 *
 * @author Chester
 */
public class RegisterVehicleServlet extends HttpServlet {

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
         response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String error = (String) request.getParameter("error");
        String successMessage = (String) request.getParameter("successMessage");
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Register Vehicle - PTFMS</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<link rel=\"stylesheet\" href=\"vehicle.css\">  ");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container-register'>");
        out.println("<div class='nav'>");
        out.println("<a href='ShowVehicleList'>Vehicle List</a>");
        out.println("<a href='" + request.getContextPath() + "/maintenance?action=dashboard'>Maintenance Dashboard</a>");
        out.println("</div>");
        out.println("<h1>Register New Vehicle</h1>");
        
        if (error != null) {
            out.println("<div class='error'>All fields must not be empty</div>");
        }
        
        if ("success".equals(request.getParameter("successMessage"))) {
            out.println("<div class='success'>Vehicle Successfully Added</div>");
        }
        
        out.println("<form method='post'>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='vehicleNumber'>Vehicle Number *</label>");
        out.println("<input type='text' id='vehicleNumber' name='vehicleNumber' placeholder='e.g., BUS-101'>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='vehicleType'>Vehicle Type *</label>");
        out.println("<select id='vehicleType' name='vehicleType'>");
        out.println("<option value=''>Select Vehicle Type</option>");
        out.println("<option value='Diesel Bus'>Diesel Bus</option>");
        out.println("<option value='Electric Light Rail'>Electric Light Rail</option>");
        out.println("<option value='Diesel-Electric Train'>Diesel-Electric Train</option>");
        out.println("</select>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='fuelType'>Fuel Type</label>");
        out.println("<input type='text' id='fuelType' name='fuelType' placeholder='e.g., Diesel, Electricity, CNG'>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='consumptionRate'>Consumption Rate *</label>");
        out.println("<input type='number' step='0.01' id='consumptionRate' name='consumptionRate' placeholder='e.g., 35.5'>");
        out.println("<small>Liters per 100km for buses/trains, kWh per mile for electric vehicles</small>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='maxPassengers'>Maximum Passengers *</label>");
        out.println("<input type='number' id='maxPassengers' name='maxPassengers' placeholder='e.g., 60'>");
        out.println("</div>");
        
        out.println("<div class='form-group'>");
        out.println("<label for='assignedRoute'>Assigned Route</label>");
        out.println("<input type='text' id='assignedRoute' name='assignedRoute' placeholder='e.g., Route 7, Line 1'>");
        out.println("</div>");
        
        out.println("<button type='submit'>Register Vehicle</button>");
        out.println("<button type='button' onclick=\"location.href='ShowVehicleList'\">Cancel</button>");
        
        out.println("</form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
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
        String vehicleNumber = request.getParameter("vehicleNumber");
        String vehicleType = request.getParameter("vehicleType");
        String fuelType = request.getParameter("fuelType");
        double consumptionRate = Double.parseDouble(request.getParameter("consumptionRate"));
        int maxPassengers = Integer.parseInt(request.getParameter("maxPassengers"));
        String route = request.getParameter("assignedRoute");
        
        if (vehicleNumber.isEmpty() || vehicleType.isEmpty() || fuelType.isEmpty()
                || consumptionRate < 0 || maxPassengers < 0 || route.isEmpty()){
            response.sendRedirect("RegisterVehicle?error");
        }else{
//            Vehicle vehicle = new Vehicle();
//            VehicleLogic logic = new VehicleLogic();
//            
//            vehicle = Vehicle.builder()
//                        .vehicleNumber(vehicleNumber)
//                        .vehicleType(vehicleType)
//                        .fuelType(fuelType)
//                        .consumptionRate(consumptionRate)
//                        .maxCapacity(maxPassengers)
//                        .route(route)
//                        .build();
//            
//            logic.addVehicle(vehicle);
            response.sendRedirect("RegisterVehicle?successMessage=success");
        }
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
