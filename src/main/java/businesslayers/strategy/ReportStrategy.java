package businesslayers.strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Strategy interface for generating reports.
 */
public interface ReportStrategy {

    /**
     * Generates a report using the given request parameters.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return a map containing the report data
     */
    Map<String, Object> generate(HttpServletRequest request, HttpServletResponse response);

    /**
     * @return the report title
     */
    String getReportTitle();
}
