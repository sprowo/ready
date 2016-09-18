package com.prowo.ymlchain.yml.model.impl;

import com.prowo.ymlchain.yml.exception.ChainMethodIlleagleException;
import com.prowo.ymlchain.yml.exception.YmlChainModelConfigException;
import com.prowo.ymlchain.yml.exception.YmlClassNotFoundException;
import com.prowo.ymlchain.yml.model.ChainBeanBuilder;
import com.prowo.ymlchain.yml.model.IHandler;
import com.prowo.ymlchain.yml.model.IHandlerContext;
import com.prowo.ymlchain.yml.ydnamic.YmlClassManager;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * yml chain的数据结构和运行方法
 */
public class YmlChainBean {
    private static Logger logger = Logger.getLogger(YmlChainBean.class);
    private static String FTL_PREFIX = ".ftl.";
    private static String FTL_SUFFIX = ".ftl";

    private String start;
    private String error;
    private String exception;
    private String end;

    private Map<String, Map<String, String>> chainMaps;

    private Map<String, String> classMap;

    private Map<String, Object> configMap;

    private YmlClassManager classManager;

    public YmlClassManager getClassManager() {
        return classManager;
    }

    public void setClassManager(YmlClassManager classManager) {
        this.classManager = classManager;
    }


    /**
     * <pre>
     * build and check YmlChainBean
     * 1.<b>[start]</b> can not be empty
     * 2.<b>[exception]</b> can not be empty
     * 3.<b>[classMap]</b> can not be empty
     * when 1-3 encountered,will throw YmlChainModelConfigException.
     *
     * class loaded with YdClassManager.findClass
     * </pre>
     *
     * @param builder
     * @throws Exception
     */
    public YmlChainBean(ChainBeanBuilder builder, YmlClassManager classManager) throws Exception {
        this.classManager = classManager;

        if (builder.getStart() == null || builder.getStart().isEmpty()) {
            logger.error("[start] can not be empty!");
            throw new YmlChainModelConfigException("[start] can not be empty!");
        }
        this.setStart(builder.getStart());

        if (builder.getException() == null || builder.getException().isEmpty()) {
            logger.error("[exception] can not be empty!");
            throw new YmlChainModelConfigException("[exception] can not be empty!");
        }
        this.setException(builder.getException());

        if (builder.getClassMap() == null || builder.getClassMap().isEmpty()) {
            logger.error("[classMap] can not be empty!");
            throw new YmlChainModelConfigException("[classMap] can not be empty!");
        }

        if (builder.getError() == null || builder.getError().isEmpty()) {
            logger.warn("[error] is empty!");
        } else {
            this.setError(builder.getError());
        }

        if (builder.getEnd() == null || builder.getEnd().isEmpty()) {
            logger.info("[end] is empty!");
        } else {
            this.setEnd(builder.getEnd());
        }

        this.classMap = builder.getClassMap();
        for (String class_name : classMap.keySet()) {
            classManager.getHandlerClass(getFullClassName(class_name));
        }
        this.setChainMaps(builder.getChainMaps());
        this.setConfigMap(builder.getConfigMap());
    }

    /**
     * <pre>
     * start the yaml from [start]
     * if encounter exception ,then invoke [exception],
     * if encounter error,then invoke [error],
     * if [end] is defined,after normal and [exception] invoked, invoke [end].
     *
     * </pre>
     *
     * @param context
     * @return returnCode the last invoked methodEntryKey
     * @throws Exception
     */
    public String startChain(IHandlerContext context) throws Exception {
        context.putConfigs(getConfigMap());
        String returnCode = null;
        try {
            try {
                returnCode = invokeMethod(getStart(), context);
                if (getEnd() != null) {
                    returnCode = invokeMethod0(getEnd(), context, false);
                }
            } catch (Exception e) {
                if (getEnd() != null) {
                    returnCode = invokeMethod0(getException(), context, false);
                    returnCode = invokeMethod0(getEnd(), context, false);
                } else {
                    returnCode = invokeMethod(getException(), context);
                }
            }

        } catch (Throwable e) {
            try {
                returnCode = invokeMethod0(getError(), context, false);
            } catch (Throwable e2) {
                logger.error("encounter error when invoke [error]");
                throw new RuntimeException("encounter error when invoke [error]");
            }
        }
        return returnCode;
    }

    /**
     * <pre>
     * start the yaml from [start]
     * if encounter exception ,then invoke [exception],
     * if encounter error,then invoke [error],
     * if [end] is defined,after normal and [exception] invoked, invoke [end].
     *
     * </pre>
     *
     * @param context
     * @return returnCode the last invoked methodEntryKey
     * @throws Exception
     */
    public String startChain(String methodEntryKey, IHandlerContext context) throws Exception {
        context.putConfigs(getConfigMap());
        String returnCode = null;
        try {
            try {
                returnCode = invokeMethod(methodEntryKey, context);
                if (getEnd() != null) {
                    returnCode = invokeMethod0(getEnd(), context, false);
                }
            } catch (Exception e) {
                if (getEnd() != null) {
                    returnCode = invokeMethod0(getException(), context, false);
                    returnCode = invokeMethod0(getEnd(), context, false);
                } else {
                    returnCode = invokeMethod(getException(), context);
                }
            }

        } catch (Throwable e) {
            try {
                returnCode = invokeMethod0(getError(), context, false);
            } catch (Throwable e2) {
                logger.error("encounter error when invoke [error]");
                throw new RuntimeException("encounter error when invoke [error]");
            }
        }
        return returnCode;
    }

    /**
     * <pre>
     * invoke method with ClassName.methodName or template
     * </pre>
     *
     * @param methodEntryKey
     * @param context
     * @return returnCode the last invoked methodEntryKey or a method which
     * return null or "".
     * @throws Throwable
     */
    public String invokeMethod(String methodEntryKey, IHandlerContext context) throws Throwable {
        String returnCode = (String) invokeMethod0(methodEntryKey, context);
        if (returnCode != null && !returnCode.isEmpty()) {
            Map<String, String> pluginMap = getChainMaps().get(methodEntryKey);
            if (pluginMap == null) {
                logger.warn("YAML auto terminated after method:" + methodEntryKey.toString());
                return methodEntryKey;
            }
            String nextMethodEntryKey = pluginMap.get(returnCode);
            if (nextMethodEntryKey == null || nextMethodEntryKey.isEmpty()) {
                logger.warn("methodStr is not defined in chainMap!");
                return methodEntryKey;
            }
            return invokeMethod(nextMethodEntryKey, context);
        }
        return returnCode;
    }

    /**
     * @param methodEntryKey ClassName.methodName
     * @param context
     * @return
     * @throws Throwable
     */
    private String invokeMethod0(String methodEntryKey, IHandlerContext context) throws Throwable {
        return invokeMethod0(methodEntryKey, context, true);
    }

    /**
     * @param methodEntryKey ClassName.methodName
     * @param context
     * @param loop           decide if loop when encountered error
     * @return
     * @throws Throwable
     */
    private String invokeMethod0(String methodEntryKey, IHandlerContext context, boolean loop) throws Throwable {
        if (methodEntryKey.startsWith(FTL_PREFIX) || methodEntryKey.endsWith(FTL_SUFFIX)) {
            if (logger.isDebugEnabled()) {
                logger.debug("YAML terminated using :" + methodEntryKey.toString());
            }
            return methodEntryKey;
        }

        String[] class_method = methodEntryKey.split("\\.");

        if (class_method.length != 2) {
            throw new RuntimeException("yaml methodEntryKey:[" + methodEntryKey + "] must be like ClassName.mehodName");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[" + methodEntryKey + "| start]");
        }
        String class_name = class_method[0];

        String method_name = class_method[1];

        Class<IHandler> clazz = classManager.getHandlerClass(getFullClassName(class_name));

        IHandler handler = classManager.getHandler(clazz);

        Method methodObj = classManager.getMethod(clazz, method_name);

        try {
            String returnCode = (String) methodObj.invoke(handler, context);
            if (logger.isDebugEnabled()) {
                logger.debug("[" + methodEntryKey + "| end]:" + returnCode);
            }
            return returnCode;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new ChainMethodIlleagleException("method:" + methodObj.getName() + " is illegal!");
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new ChainMethodIlleagleException("check the arguments of method:" + methodObj.getName());
        } catch (InvocationTargetException e) {
            logger.warn(methodObj.toString() + ":", e.getTargetException());
            if (loop) {
                context.setThrowable(e.getTargetException());
                throw e.getTargetException();
            }
            return methodEntryKey;
        } catch (Throwable t) {
            logger.error(methodObj.toString() + ":", t);
            throw t;
        }

    }

    /**
     * @param class_name
     * @return
     * @throws YmlClassNotFoundException
     */
    private String getFullClassName(String class_name) throws YmlClassNotFoundException {

        String fullName = classMap.get(class_name);
        if (fullName != null && !fullName.isEmpty()) {
            return fullName;
        } else {
            logger.error(class_name + " is not defined in ClassMap!");
            throw new YmlClassNotFoundException(class_name + " is not defined in ClassMap!");
        }
    }


    public void setStart(String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void setChainMaps(Map<String, Map<String, String>> chainMaps) {
        this.chainMaps = chainMaps;
    }

    public Map<String, Map<String, String>> getChainMaps() {
        return chainMaps;
    }

    public static String getTmplSuffix() {
        return FTL_SUFFIX;
    }

    public static String getTmplPrefix() {
        return FTL_PREFIX;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }
}
