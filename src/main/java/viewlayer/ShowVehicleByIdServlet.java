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
public class ShowVehicleByIdServlet extends HttpServlet {

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
        String vehicleIdStr = request.getParameter("id");
        
        if (vehicleIdStr == null || vehicleIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/vehicles?action=list");
            return;
        }

        try {
            int vehicleId = Integer.parseInt(vehicleIdStr);
            VehicleLogic logic = new VehicleLogic();
            Vehicle vehicle = logic.getVehicleById(vehicleId);

            if (vehicle == null) {
                response.sendRedirect(request.getContextPath() + "/vehicles?action=list");
                return;
            }

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Vehicle Details - " + vehicle.getNumber() + "</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link rel=\"stylesheet\" href=\"vehicle.css\">  ");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container-id'>");
            out.println("<div class='nav'>");
            out.println("<a href='ShowVehicleList'>Back to Vehicle List</a>");
            out.println("<a href='" + request.getContextPath() + "/maintenance?action=dashboard'>Maintenance Dashboard</a>");
            out.println("</div>");
            out.println("<h1>Vehicle Details: " + vehicle.getNumber() + "</h1>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Vehicle ID:</span>");
            out.println("<span class='detail-value'>" + vehicle.getVehicleId() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Vehicle Number:</span>");
            out.println("<span class='detail-value'>" + vehicle.getNumber() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Vehicle Type:</span>");
            out.println("<span class='detail-value'>" + vehicle.getVehicleType() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Fuel Type:</span>");
            out.println("<span class='detail-value'>" + vehicle.getFuelType() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Consumption Rate:</span>");
            out.println("<span class='detail-value'>" + vehicle.getConsumptionRate() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Maximum Passengers:</span>");
            out.println("<span class='detail-value'>" + vehicle.getMaxCapacity() + "</span>");
            out.println("</div>");
            
            out.println("<div class='detail-row'>");
            out.println("<span class='detail-label'>Assigned Route:</span>");
            out.println("<span class='detail-value'>" + vehicle.getRoute() + "</span>");
            out.println("</div>");
            
            out.println("<div style='margin-top: 30px;'>");
            out.println("<a href='ShowVehicleList' class='btn'>Back to List</a>");
            out.println("<a href='" + request.getContextPath() + "/maintenance?action=schedule&vehicleId=" + vehicle.getVehicleId() + "' class='btn'>Schedule Maintenance</a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "ShowVehicleList");
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
