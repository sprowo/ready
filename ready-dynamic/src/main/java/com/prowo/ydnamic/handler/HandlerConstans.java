package com.prowo.ydnamic.handler;

public interface HandlerConstans {
    /**
     * HandlerContext用key：保存yaml中的处理结果对象
     */
    String RESPONSR_MODEL = "RESPONSR_MODEL";
    /**
     * HandlerContext用key：保存yaml中的request请求数据
     */
    String REQUEST_DATA = "REQUEST_DATA";
    /**
     * HandlerContext用key：调用外部接口 临时对象
     */
    String TEMP_OBJECT = "TEMP_OBJECT";
    /**
     * HandlerContext用key：调用外部接口请求数据字符串
     */
    String TEMP_REQUEST_DATA = "TEMP_REQUEST_DATA";
    /**
     * HandlerContext用key：调用外部接口 返回数据字符串
     */
    String TEMP_RESPONSE_DATA = "TEMP_RESPONSE_DATA";
    /**
     * HandlerContext用key：记录接口调用
     */
    String RECORD_SERVICE = "RECORD_SERVICE";

    String STATUS_OK = "ok";
    String STATUS_NG = "ng";

    String CACHE_KEY_CONFIG = "CACHE_KEY_CONFIG";
    String CACHE_KEY_DICT = "CACHE_KEY_DICT";
    String CACHE_KEY_USER = "CACHE_KEY_USER";
    String CACHE_KEY_EXCEPTION = "CACHE_KEY_EXCEPTION";
    String CACHE_KEY_BRANCH = "CACHE_KEY_BRANCH";
    String CACHE_KEY_IPADDRESS = "CACHE_KEY_IPADDRESS";
    String CACHE_KEY_CLIENT = "CACHE_KEY_CLIENT";
    String CACHE_KEY_MESSAGE = "CACHE_KEY_MESSAGE";
}
