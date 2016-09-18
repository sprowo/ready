package com.prowo.persist;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

public abstract class BaseVO implements Serializable {

    private static final long serialVersionUID = 1707293635736153082L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
