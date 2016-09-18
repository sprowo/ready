package com.prowo.ymlchain.yml.model.impl;

import com.prowo.ymlchain.yml.model.IHandlerContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * yml 文件的标准上下文实现
 */
public class StandardHandlerContext implements IHandlerContext {
    private HttpServletRequest request = null;
    private HttpServletResponse response = null;

    private Map<String, Object> responseMap = null;

    private Map<String, Object> conversationObjs = null;

    private Map<String, String[]> parameters = null;

    private Map<String, Object> configs = new HashMap<String, Object>();

    private Throwable t;

    public StandardHandlerContext() {
        parameters = new HashMap<String, String[]>();
    }

    public StandardHandlerContext(HttpServletRequest request) {
        this();
        setHttpServletRequest(request);
    }

    public StandardHandlerContext(HttpServletRequest request, HttpServletResponse response) {
        this();
        setHttpServletRequest(request);
        setHttpServletResponse(response);
    }

    public StandardHandlerContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> configs) {
        this(request, response);
        putConfigs(configs);
    }


    @Override
    public void setRequestParameter(String name, String value) {
        setRequestParameter(name, new String[]{value});
    }

    @Override
    public void setRequestParameter(String name, String values[]) {
        parameters.put(name, values);
    }

    public void setRequestParameters(Map<String, String[]> params) {
        for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext(); ) {
            Object key = iterator.next();
            Object value = params.get(key);
            if (value instanceof String)
                setRequestParameter((String) key, (String) value);
            else if (value instanceof String[])
                setRequestParameter((String) key, (String[]) value);
            else
                throw new IllegalArgumentException((new StringBuilder(
                        "Parameter map value must be single value  or array of type [")).append(String.class.getName())
                        .append("]").toString());
        }

    }

    @Override
    public String getRequestParameter(String name) {
        String arr[] = (String[]) parameters.get(name);
        return arr == null || arr.length <= 0 ? null : arr[0];
    }

    public void setThrowable(Throwable t) {
        this.t = t;
    }

    public Throwable getThrowable() {
        return t;
    }

    @Override
    public HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    public void setHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public HttpServletResponse getHttpServletResponse() {
        return this.response;
    }

    public void setHttpServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public Map<String, Object> getResponseAttributes() {
        return this.responseMap;
    }

    @Override
    public void putResponseAttribute(String key, Object obj) {
        if (responseMap == null) {
            this.responseMap = new HashMap<String, Object>();
        }
        responseMap.put(key, obj);

    }

    @Override
    public Object getConverzationObj(String key) {
        if (conversationObjs == null) {
            conversationObjs = new HashMap<String, Object>();
        }
        return conversationObjs.get(key);
    }

    @Override
    public void setConverzationObj(String key, Object obj) {
        if (conversationObjs == null) {
            conversationObjs = new HashMap<String, Object>();
        }
        conversationObjs.put(key, obj);
    }

    @Override
    public void putConfigs(Map<String, Object> configs) {
        if (this.configs == null) {
            this.configs = new HashMap<String, Object>();
        }
        this.configs.putAll(configs);

    }

    @Override
    public Object getConfig(String key) {
        return configs.get(key);
    }

    @Override
    public String getConverzationStr(String key) {
        return (String) conversationObjs.get(key);
    }

    @Override
    public void setConverzationStr(String key, String str) {
        if (conversationObjs == null) {
            conversationObjs = new HashMap<String, Object>();
        }
        conversationObjs.put(key, str);
    }

    @Override
    public Map<String, String[]> getRequestParameterMap() {
        return parameters;
    }

}
