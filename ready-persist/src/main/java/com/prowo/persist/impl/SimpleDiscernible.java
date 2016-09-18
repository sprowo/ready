package com.prowo.persist.impl;

import com.prowo.persist.Discernible;
import com.prowo.persist.Objectx;

/**
 * 可识别对象简单实现
 */
public class SimpleDiscernible extends Objectx implements Discernible {

    private String id;

    private String name;

    public SimpleDiscernible() {
        super();
    }

    public SimpleDiscernible(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

}
