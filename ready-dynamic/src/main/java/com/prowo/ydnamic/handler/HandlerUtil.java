package com.prowo.ydnamic.handler;

import com.prowo.ymlchain.yml.model.IHandlerContext;
import com.prowo.ymlchain.yml.model.impl.StandardHandlerContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerUtil {
    public static IHandlerContext getInnerHandlerContextInstance(HttpServletRequest request) {
        StandardHandlerContext context = new StandardHandlerContext();
        context.setHttpServletRequest(request);
        return context;
    }

    /**
     * 获取标准 yml 上下文
     *
     * @param request
     * @param response
     */
    public static IHandlerContext getHandlerContextInstance(HttpServletRequest request, HttpServletResponse response) {
        StandardHandlerContext context = new StandardHandlerContext();
        context.setHttpServletRequest(request);
        context.setRequestParameters(request.getParameterMap());
        context.setHttpServletResponse(response);
        return context;
    }

}
