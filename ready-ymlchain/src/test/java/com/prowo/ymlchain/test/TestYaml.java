package com.prowo.ymlchain.test;

import com.prowo.ymlchain.yml.model.impl.StandardHandlerContext;
import com.prowo.ymlchain.yml.model.impl.YmlChainDriver;

public class TestYaml {
    public static void main(String[] args) throws Throwable {

        final String fileName = "com/prowo/ymlchain/test/test.yml";
        YmlChainDriver ycd = new YmlChainDriver();
        ycd.setYamlPath(TestYaml.class.getClassLoader().getResources("/") + fileName);
        // YmlChainDriver.setYamlPath("testp/");
        long s = System.currentTimeMillis();
//
//         YdClassManager.setPath("D:/yd_workspace/yd-ymlchain/lib/testlib/");
//         YdClassManager.load();
        String chain = ycd.startChainByMethod(fileName, "Test1.method2", new StandardHandlerContext());
        //  String chain = ycd.startChain(fileName, new StandardHandlerContext());

        System.err.println(chain);
        System.out.println("总耗时:" + (System.currentTimeMillis() - s) + "毫秒");
        Thread.sleep(1000);
        // YdClassManager.load();
        // YmlChainDriver.reloadChain(fileName);
        // YmlChainDriver.startChain(fileName, new StandardHandlerContext());
    }
}
