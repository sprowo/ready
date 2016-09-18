package com.prowo.ymlchain.test;

import com.prowo.ymlchain.yml.model.IHandler;
import com.prowo.ymlchain.yml.model.IHandlerContext;

public class Test2 implements IHandler {

    public String method1(IHandlerContext hContext) {
        throw new RuntimeException();
        // return "ok";
    }

    public String method2(IHandlerContext hContext) {
        Throwable t = hContext.getThrowable();
        System.err.println(t.getStackTrace()[0].getClassName().substring(
                t.getStackTrace()[0].getClassName().lastIndexOf(".") + 1, t.getStackTrace()[0].getClassName().length())
                + "." + t.getStackTrace()[0].getMethodName());
        System.err.println(hContext);
        return "ok";
    }
}
