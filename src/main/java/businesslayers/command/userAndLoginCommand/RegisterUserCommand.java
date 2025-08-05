package businesslayers.command.userAndLoginCommand;

import businesslayers.command.Command;
import dataaccesslayer.users.UserDAO;
import dataaccesslayer.users.UserDAOImpl;
import dataaccesslayer.users.User;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command that registers a new user account.
 */
public class RegisterUserCommand implements Command {

    private final UserDAO userDAO = new UserDAOImpl();

    /**
     * Handles user registration. If the email is already in use, forwards back to the
     * registration page with an error. Otherwise, creates the user and redirects to login.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = "Operator";

        if (userDAO.findUserByEmail(email).isPresent()) {
            request.setAttribute("error", "An account with this email already exists.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
            return;
        }

        User newUser = new User(name, email, password, userType);
        userDAO.addUser(newUser);

        request.getSession().setAttribute("message", "Registration successful! Please log in.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
