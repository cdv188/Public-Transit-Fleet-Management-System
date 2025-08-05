package dataaccesslayer.filter;

import dataaccesslayer.users.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet filter that enforces authentication and role-based access control.
 */
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = requestURI.substring(contextPath.length());

        User user = null;
        boolean isAuthenticated = false;
        boolean isManager = false;

        if (session != null && session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
            isAuthenticated = true;
            isManager = "Manager".equals(user.getUserType());
        }

        boolean requiresAuth = servletPath.startsWith("/ShowVehicle")
                || servletPath.startsWith("/RegisterVehicle")
                || servletPath.startsWith("/ShowMaintenance")
                || servletPath.startsWith("/dashboard.jsp");

        boolean requiresManager = servletPath.startsWith("/RegisterVehicle")
                || (servletPath.startsWith("/ShowMaintenance")
                && "schedule".equals(request.getParameter("action")));

        if (!requiresAuth) {
            chain.doFilter(request, response);
            return;
        }

        if (!isAuthenticated) {
            response.sendRedirect(contextPath + "/login.jsp?error=login_required");
            return;
        }

        if (requiresManager && !isManager) {
            if ("POST".equals(request.getMethod())) {
                response.sendRedirect(contextPath + "/dashboard.jsp?error=unauthorized");
            } else {
                response.sendRedirect(contextPath + "/dashboard.jsp?error=access_denied");
            }
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
