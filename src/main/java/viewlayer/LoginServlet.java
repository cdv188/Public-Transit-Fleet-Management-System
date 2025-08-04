package viewlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Chester
 */
public class LoginServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Login</title>");
            out.println("<link rel=\"stylesheet\" href=\"style.css\">");
            out.println("</head>");
            out.println("<body class=\"client\">");
            out.println("    <div class=\"auth-page\">\n" +
                        "        <div class=\"auth-container\">\n" +
                        "            <h2>Login</h2>\n" +
                        "            <form id=\"login-form\" method=\"POST\">\n" +
                        "                <h1>PTFMS Login</h1>\n" +
                        "                <input type=\"text\" id=\"username\" placeholder=\"Username\" name=\"username\">\n" +
                        "                <input type=\"password\" id=\"password\" placeholder=\"Password\" name=\"password\">\n" +
                        "                <button type=\"submit\" name=\"submit\">Login</button>\n" +
                        "            </form>\n" +
                        "            <p class=\"register\">Don't have an account? <a href=\"register.html\">Register here</a></p>\n" +
                        "        </div>\n" +
                        "    </div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    /**
     * Handles POST requests to validate credentials
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Properties props = new Properties();
        try(InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("database.properties")){
            props.load(in);
        }catch(IOException e){
            e.printStackTrace();
        }
        String usr = props.getProperty("jdbc.username");
        String pass = props.getProperty("jdbc.password");
        // Validate credentials
        if (usr.equals(username) && pass.equals(password)) {
            request.getSession().setAttribute("authenticated", true);
            response.sendRedirect("ShowVehicleList");
        } else {
            response.sendRedirect("Login?error=true");
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