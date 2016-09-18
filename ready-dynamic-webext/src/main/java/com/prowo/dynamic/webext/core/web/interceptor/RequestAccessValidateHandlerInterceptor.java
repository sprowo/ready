package com.prowo.dynamic.webext.core.web.interceptor;

import com.prowo.ydnamic.cache.CacheManager;
import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.handler.HandlerConstans;
import com.prowo.ydnamic.handler.MapModel;
import com.prowo.ydnamic.record.Recorder;
import com.prowo.ydnamic.web.IPAddress;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 请求的频率验证
 */
public class RequestAccessValidateHandlerInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestAccessValidateHandlerInterceptor.class);

    private ComxContext context = ComxContext.getContext();

    private CacheManager cacheManager = ComxContext.getContext().getCacheManager();

    public RequestAccessValidateHandlerInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> manager = cacheManager.getMap(HandlerConstans.CACHE_KEY_IPADDRESS);
        String ipAddress = IPAddress.getIpAddr(request);
        String rate = manager.get(ipAddress);
        if (rate == null) {
            rate = new Date().getTime() + "-" + 1;
            manager.put(ipAddress, rate);
        } else {
            Long now = System.currentTimeMillis();
            StringTokenizer token = new StringTokenizer(rate, "-");
            long lastTime = NumberUtils.toLong(token.nextToken());
            int count = NumberUtils.toInt(token.nextToken());

            if (now - lastTime < Integer.valueOf(context.get("system.time.zone"))) {
                MapModel model = MapModel.getModelInstance();
                PrintWriter writer = response.getWriter();
                String newRate = lastTime + "-" + (count + 1);
                manager.put(ipAddress, newRate);
                if (count > Integer.valueOf(context.get("system.max.count"))) {
                    // 超过访问频率
                    logger.warn("用户恶意操作,IP---" + ipAddress);
                    model.setResult(false);
                    model.setCode("m04");
                    writer.print(model.toJson());
                    writer.close();
                    Recorder.setCustomerIp(ipAddress);
                    return false;
                }
            }
        }
        return true;
    }

}
