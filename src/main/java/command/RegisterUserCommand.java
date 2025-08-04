package command;

import userDAO.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterUserCommand implements Command {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = "Operator"; 

        if (userDAO.findUserByEmail(email).isPresent()) {
            request.setAttribute("error", "An account with this email already exists.");
            // Forward back to the registration page to show the error
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
            return; // Stop further execution
        }

        User newUser = new User(name, email, password, userType);
        userDAO.addUser(newUser);

        // Redirect to login page with a success message
        // Use session to pass message across a redirect
        request.getSession().setAttribute("message", "Registration successful! Please log in.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}