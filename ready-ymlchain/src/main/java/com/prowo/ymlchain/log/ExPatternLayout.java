package com.prowo.ymlchain.log;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

public class ExPatternLayout extends PatternLayout {

    public ExPatternLayout(String pattern) {
        super(pattern);
    }

    public ExPatternLayout() {
        super();
    }

    /**
     * 重写createPatternParser方法，返回PatternParser的子类
     */
    @Override
    protected PatternParser createPatternParser(String pattern) {
        return new ExPatternParser(pattern);
    }
}