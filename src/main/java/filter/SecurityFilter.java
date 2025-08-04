package filter;

import dataaccesslayer.users.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * SecurityFilter - Controls access to different parts of the application
 * - Managers: Full access to all features
 * - Operators: Limited access (view-only for most features)
 * - Unauthenticated: Redirected to login
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
        
        // Extract the servlet path
        String servletPath = requestURI.substring(contextPath.length());
        
        // Check if user is authenticated
        User user = null;
        boolean isAuthenticated = false;
        boolean isManager = false;
        
        if (session != null && session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
            isAuthenticated = true;
            isManager = "Manager".equals(user.getUserType());
        }
        
        // Define which resources require authentication
        boolean requiresAuth = servletPath.startsWith("/ShowVehicle") ||
                              servletPath.startsWith("/RegisterVehicle") ||
                              servletPath.startsWith("/ShowMaintenance") ||
                              servletPath.startsWith("/dashboard.jsp");
        
        // Define which resources require Manager role
        boolean requiresManager = servletPath.startsWith("/RegisterVehicle") ||
                                 (servletPath.startsWith("/ShowMaintenance") && 
                                  "schedule".equals(request.getParameter("action")));
        
        // Allow access to public resources
        if (!requiresAuth) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check authentication
        if (!isAuthenticated) {
            response.sendRedirect(contextPath + "/login.jsp?error=login_required");
            return;
        }
        
        // Check manager privileges for restricted actions
        if (requiresManager && !isManager) {
            // For AJAX/form submissions, redirect to error page
            if ("POST".equals(request.getMethod())) {
                response.sendRedirect(contextPath + "/dashboard.jsp?error=unauthorized");
                return;
            }
            // For regular navigation, redirect to dashboard with error
            response.sendRedirect(contextPath + "/dashboard.jsp?error=access_denied");
            return;
        }
        
        // User has appropriate access, continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}