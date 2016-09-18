package com.prowo.dynamic.webext.core.web.filter;

import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.context.SystemConstants;
import com.prowo.ydnamic.session.Session;
import com.prowo.ydnamic.validation.Validate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestLoginFilter implements Filter {
    private List<String> paths;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        for (String requestURI : paths) {
            if (uri.contains(requestURI)) {
                // 直接放行
                chain.doFilter(request, response);
                return;
            }
        }
        String innerExcludePaths = ComxContext.getContext().get("login.filter.exclude.paths");
        if (!Validate.isNull(innerExcludePaths)) {
            List<String> innerPaths = new ArrayList<String>();
            innerPaths = Arrays.asList(innerExcludePaths.split("\\s*,\\s*"));
            for (String requestURI : innerPaths) {
                if (uri.contains(requestURI)) {
                    // 直接放行
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        Session session = new Session(request, response);
        String user = session.getAttribute(SystemConstants.SESSION_USER_INFO);

        if (user == null) {
            response.addHeader("location", request.getContextPath() + "/login.html");
            response.setStatus(302);
            return;
        }
        ComxContext context = ComxContext.getContext();
        if (context.get(user) == null) {
            response.addHeader("location", request.getContextPath() + "/login.html");
            response.setStatus(302);
            return;
        }

        session.setAttribute(SystemConstants.SESSION_USER_INFO, user);

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", -10);
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        paths = new ArrayList<String>();
        String excludePaths = filterConfig.getInitParameter("excludePaths");
        if (!Validate.isNull(excludePaths)) {
            paths = Arrays.asList(excludePaths.split("\\s*,\\s*"));
        }
    }

}
