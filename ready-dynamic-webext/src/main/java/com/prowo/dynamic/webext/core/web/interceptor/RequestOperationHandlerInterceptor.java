package com.prowo.dynamic.webext.core.web.interceptor;

import com.prowo.ydnamic.context.SystemConstants;
import com.prowo.ydnamic.mapper.JSONMapper;
import com.prowo.ydnamic.record.Recorder;
import com.prowo.ydnamic.session.Session;
import com.prowo.ydnamic.web.IPAddress;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于Interceptor记录后台操作记录
 */
public class RequestOperationHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler)
            throws Exception {
        // 代码写在这里
        Session session = new Session(httpRequest, httpResponse);
        String user = session.getAttribute(SystemConstants.SESSION_USER_INFO);

        Recorder.setRequestData(JSONMapper.toJson(httpRequest.getParameterMap()));
        Recorder.setReferer(httpRequest.getHeader("Referer"));
        Recorder.setUserAgent(httpRequest.getHeader("User-Agent"));
        Recorder.setCustomerIp(IPAddress.getIpAddr(httpRequest));
        if (httpRequest.getRequestURL().toString().indexOf("system/") > 0) {
            Recorder.setServiceId(httpRequest.getRequestURL().toString().substring(httpRequest.getRequestURL().toString().indexOf("system/")));
        } else {
            Recorder.setServiceId(httpRequest.getRequestURL().toString());
        }
        Recorder.setPartnerid(user);

        Recorder.setOpLog(true);
        return true;
    }

}
