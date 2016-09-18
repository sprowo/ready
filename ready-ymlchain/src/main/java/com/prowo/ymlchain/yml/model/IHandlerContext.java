package com.prowo.ymlchain.yml.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * yml chain的上下文
 */
public interface IHandlerContext {
    /**
     * 获取HttpServletRequest
     *
     * @return HttpServletRequest
     */
    HttpServletRequest getHttpServletRequest();

    /**
     * 获取HttpServletResponse
     *
     * @return HttpServletResponse
     */
    HttpServletResponse getHttpServletResponse();

    /**
     * 获取模板map，用于模板文件根下面
     *
     * @return ResponseAttributes
     */
    Map<String, Object> getResponseAttributes();

    /**
     * 增加模板变量
     *
     * @param key
     * @param obj
     */
    void putResponseAttribute(String key, Object obj);

    /**
     * 模拟参数输入
     *
     * @param key
     * @param value
     */
    void setRequestParameter(String name, String value);

    /**
     * 模拟参数（数组）输入
     *
     * @param key
     * @param values
     */
    void setRequestParameter(String name, String values[]);

    /**
     * 模拟参数输出
     *
     * @return
     */
    String getRequestParameter(String key);

    /**
     * 模拟参数输出map
     *
     * @return
     */
    Map<String, String[]> getRequestParameterMap();

    /**
     * 设置会话级别的参数
     *
     * @param key
     * @param obj
     */
    void setConverzationObj(String key, Object obj);

    /**
     * 获取会话级别的参数
     *
     * @return
     */
    Object getConverzationObj(String key);

    /**
     * 获取会话级别的参数
     *
     * @param key
     * @param str
     */
    void setConverzationStr(String key, String str);

    /**
     * 获取会话级别的参数
     *
     * @return
     */
    String getConverzationStr(String key);

    /**
     * 设置Throwable，用于报错处理
     *
     * @param t
     */
    void setThrowable(Throwable t);

    /**
     * 获取Throwable，用于报错处理
     *
     * @return
     */
    Throwable getThrowable();

    /**
     * 获取yml中configMap的值
     *
     * @return
     */
    Object getConfig(String key);

    /**
     * 设置全局的配置
     *
     * @return
     */
    void putConfigs(Map<String, Object> configs);

}
