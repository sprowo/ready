package com.prowo.ymlchain.yml.ydnamic;

import com.prowo.ymlchain.yml.model.impl.YmlChainDriver;

import java.util.Map;
import java.util.Set;

/**
 * yml chain的动态加载的wrapper,用于处理shadow下面的jar
 */
public class YmlChainDriverReloader {

    private YmlChainDriver ymlChainDriver;

    public void setYmlChainDriver(YmlChainDriver ymlChainDriver) {
        this.ymlChainDriver = ymlChainDriver;
    }

    public YmlChainDriver getYmlChainDriver() {
        return ymlChainDriver;
    }

    /**
     * 重新加载某个jar的ymlFile
     *
     * @throws Exception
     */
    public void reload(String fileName) throws Exception {
        // 20151013 清空yml缓存，防止引用释放缓慢，内存泄露或者map的key更新缓慢
        ymlChainDriver.clearYml();
        Set<String> ymlFiles = ymlChainDriver.getClassManager().getYmlFiles().get(fileName);
        for (String ymlFile : ymlFiles) {
            ymlChainDriver.reloadChain(ymlFile);
        }
    }

    /**
     * 重新加载所有jar的ymlFile
     *
     * @throws Exception
     */
    public void reload() throws Exception {
        // 20151013 清空yml缓存，防止引用释放缓慢，内存泄露或者map的key更新缓慢
        ymlChainDriver.clearYml();
        Map<String, Set<String>> ymlFiles = ymlChainDriver.getClassManager().getYmlFiles();
        for (Set<String> ymlNames : ymlFiles.values()) {
            for (String ymlFile : ymlNames) {
                ymlChainDriver.reloadChain(ymlFile);
            }
        }
    }

    /**
     * 加载所有jar的class
     *
     * @throws Exception
     */
    public void loadClass() {
        ymlChainDriver.getClassManager().load();
    }

}
