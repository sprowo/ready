package com.prowo.dynamic.webext.core.web.servlet;

import com.prowo.ydnamic.context.ComxContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;

/**
 * 静态资源加载servlet
 */
public class StaticResourcesServlet extends HttpServlet {
    private Properties contentTypes = new Properties();
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fileUri = request.getRequestURI();
        if (fileUri.endsWith("/") || fileUri.lastIndexOf(".") < 0) {
            response.setStatus(403);
            return;
        }
        String contextPath = request.getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath = contextPath + "/";
        }
        fileUri = fileUri.substring(contextPath.length(), fileUri.length());

        InputStream inputStream = ComxContext.getContext().getYmlChainDriver().getClassManager()
                .getResourceAsStream(fileUri);
        if (inputStream == null) {
            inputStream = getClass().getClassLoader().getResourceAsStream(fileUri);
        }
        if (inputStream == null) {
            response.setStatus(404);
            return;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        String suffix = fileUri.substring(fileUri.lastIndexOf("."), fileUri.length());
        response.setContentType(contentTypes.getProperty(suffix));
        if (suffix.equalsIgnoreCase(".jsp")) {
            PrintWriter out = response.getWriter();
            String line = null;
            while ((line = in.readLine()) != null) {
                if (!line.matches("<%@.*")) {
                    out.println(line);
                }
            }
        } else {
            ServletOutputStream output = response.getOutputStream();
            int length = 0;
            byte[] cache = new byte[1024];
            while ((length = inputStream.read(cache)) > 0) {
                output.write(cache, 0, length);
            }

            inputStream.close();
            output.flush();
            output.close();
            in.close();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("content-type.properties");
        if (in == null) {
            in = this.getClass().getResourceAsStream("content-type.properties");
        }
        try {
            contentTypes.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
