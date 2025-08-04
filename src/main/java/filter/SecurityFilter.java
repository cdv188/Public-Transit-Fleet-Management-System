package filter;

import userDAO.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        boolean isManager = false;

        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            // Check if user has the 'Manager' role from the DB
            if ("Manager".equals(user.getUserType())) {
                isManager = true;
            }
        }
        
        if (isManager) {
            chain.doFilter(request, response); // User is an authorized Manager.
        } else {
            // User is not a Manager, redirect to login page.
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}