package com.prowo.ydnamic.web;

import javax.servlet.http.HttpServletRequest;

public class IPAddress {

    /**
     * 获得IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") != -1) {
            ip = ip.replaceAll(" ", "").split("[,]")[0];
        }
        return ip;
    }
}
