package com.prowo.ymlchain.test;

import com.prowo.ymlchain.yml.model.IHandler;
import com.prowo.ymlchain.yml.model.IHandlerContext;

public class Test1 implements IHandler {

    public String method1(IHandlerContext hContext) {
        return "ok";
    }

    public String method2(IHandlerContext hContext) {
        return "ok";
    }
}
