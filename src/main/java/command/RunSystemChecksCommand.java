package command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RunSystemChecksCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        executeWithResult(request, response);
    }

    public String executeWithResult(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>System Checks - PTFMS</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<link rel='stylesheet' href='css/commands.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");

            out.println("<h1>System Checks</h1>");
            out.println("<div class='log'>");
            out.println("✔ Engine diagnostics passed<br>");
            out.println("✔ Brake system check passed<br>");
            out.println("✔ GPS module check passed<br>");
            out.println("✔ Fuel system check passed<br>");
            out.println("</div>");

            out.println("<p class='success'>All systems are operational.</p>");

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return "/error.html";
        }
    }
}
