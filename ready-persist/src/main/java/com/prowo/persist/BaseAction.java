package com.prowo.persist;

/**
 * 基础类
 */
public abstract class BaseAction extends Objectx {
    /**
     * XML view
     */
    protected static final String XMLVIEW = "marshallView";

    /**
     * JSON view
     */
    protected static final String JSONVIEW = "jsonView";

    /**
     * 判断是否为空
     *
     * @param value
     * @return
     */
    protected boolean empty(String value) {
        return value == null || value.trim().length() == 0;
    }

    protected java.sql.Date c(java.util.Date date) {
        if (date != null) {
            return new java.sql.Date(date.getTime());
        } else {
            return new java.sql.Date(0);
        }
    }
}
