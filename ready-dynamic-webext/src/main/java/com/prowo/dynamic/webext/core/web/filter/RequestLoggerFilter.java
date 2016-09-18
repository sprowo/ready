package com.prowo.dynamic.webext.core.web.filter;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

public class RequestLoggerFilter implements Filter {

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        Map<String, String[]> map = request.getParameterMap();
        StringBuilder sb = new StringBuilder("request parameters:\n");
        for (Entry<String, String[]> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + Arrays.asList(entry.getValue()) + "\n");
        }
        LoggerUtil.log(Level.DEBUG, "{0}", sb.toString());
        Enumeration<String> headers = request.getHeaderNames();
        StringBuilder sb2 = new StringBuilder("request heads:\n");
        String header;
        while (headers.hasMoreElements()) {
            header = headers.nextElement();
            sb2.append(header + "=" + Arrays.asList(request.getHeader(header)) + "\n");
        }
        LoggerUtil.log(Level.DEBUG, "{0}", sb2.toString());
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
