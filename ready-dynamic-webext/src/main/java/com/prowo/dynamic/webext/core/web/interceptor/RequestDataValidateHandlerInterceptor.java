package com.prowo.dynamic.webext.core.web.interceptor;

import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.encryption.Base64;
import com.prowo.ydnamic.encryption.MD5;
import com.prowo.ydnamic.handler.HandlerConstans;
import com.prowo.ydnamic.handler.MapModel;
import com.prowo.ydnamic.record.Recorder;
import com.prowo.ydnamic.validation.Validate;
import com.prowo.ydnamic.web.IPAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 请求的数据验证
 */
public class RequestDataValidateHandlerInterceptor extends HandlerInterceptorAdapter {

    public RequestDataValidateHandlerInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object handler)
            throws Exception {
        String request = httpRequest.getParameter("request");
        String partnerid = httpRequest.getParameter("partnerid");
        String data = httpRequest.getParameter("data");
        if (Validate.isNull(data)) {
            data = httpRequest.getParameter("xmldata");
        }
        String validation = httpRequest.getParameter("validation");

        String requestid = httpRequest.getParameter("requestid");
        if (requestid == null) {
            requestid = 'T' + String.valueOf(System.currentTimeMillis());
        }

        Recorder.setPartnerid(partnerid);
        Recorder.setCustomerIp(IPAddress.getIpAddr(httpRequest));
        Recorder.setRequestData(Base64.decode(data));
        Recorder.setServiceId(request);
        Recorder.setRequestid(requestid);
        Recorder.setVersion(httpRequest.getParameter("version"));


        MapModel model = MapModel.getModelInstance();
        PrintWriter writer = null;
        //验证请求的合作商和请求的接口 在dicts表中是否定义
        if (!ComxContext.getContext().getCacheManager().getMap(HandlerConstans.CACHE_KEY_DICT).containsKey(partnerid + "/" + request)) {
            model.setResult(false);
            model.setCode("m01");
            writer = httpResponse.getWriter();
            writer.print(model.toJson());
            writer.close();
            return false;
        }
        if (StringUtils.isEmpty(data)) {
            model.setResult(false);
            model.setCode("m02");
            writer = httpResponse.getWriter();
            writer.print(model.toJson());
            writer.close();
            return false;
        }
        //合作商中取密码
        String pass = ComxContext.getContext().get(partnerid);
        //将得到的data加上请求的合作商再加上请求的接口 进行MD5， 之后与validation 比较 通过测验证通过，反之
        String expectedValidation = MD5.md5(new StringBuilder().append(data).append(partnerid).append(pass).toString());
        if (StringUtils.isEmpty(validation) || !validation.equals(expectedValidation)) {
            model.setResult(false);
            model.setCode("m03");
            writer = httpResponse.getWriter();
            writer.print(model.toJson());
            writer.close();
            return false;
        }
        return true;
    }

}
