package com.prowo.ymlchain.yml.model;

import java.util.Map;

public class ChainBeanBuilder {

    private String start;
    private String error;
    private String exception;
    private String end;

    private Map<String, Map<String, String>> chainMaps;

    private Map<String, String> classMap;

    private Map<String, Object> configMap;

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

    public void setChainMaps(Map<String, Map<String, String>> chainMaps) {
        this.chainMaps = chainMaps;
    }

    public Map<String, Map<String, String>> getChainMaps() {
        return chainMaps;
    }

    public void setClassMap(Map<String, String> classMap) {
        this.classMap = classMap;
    }

    public Map<String, String> getClassMap() {
        return classMap;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }

}
