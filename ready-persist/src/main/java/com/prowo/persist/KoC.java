package com.prowo.persist;

/**
 * 键值对，值为任意对象
 */
public class KoC {
    private String key;

    private String optor = "=";

    private Object value;

    public KoC() {
        super();
    }

    public KoC(String key, Object value) {
        super();
        this.key = key;
        this.value = value;
    }

    public KoC(String key, String optor, Object value) {
        super();
        this.key = key;
        this.optor = optor;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOptor() {
        return optor;
    }

    public void setOptor(String optor) {
        this.optor = optor;
    }
}
