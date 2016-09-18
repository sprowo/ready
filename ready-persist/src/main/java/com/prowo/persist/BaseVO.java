package com.prowo.persist;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public abstract class BaseVO implements Serializable {

    private static final long serialVersionUID = 1707293635736153082L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
