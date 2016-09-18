package com.prowo.ydnamic.context;

import com.prowo.persist.Objectx;
import com.prowo.ydnamic.cache.CacheManager;
import com.prowo.ydnamic.persist.ComxPagingDAO;
import com.prowo.ymlchain.yml.model.impl.YmlChainDriver;
import com.prowo.ymlchain.yml.ydnamic.YmlChainDriverReloader;

import java.io.IOException;

/**
 * 框架全局上下文基类
 * 1.缓存的获取和刷新
 * 2.获取dao
 * 3.动态加载spring配置文件
 * 4.yml和动态classloader管理
 */
public abstract class ComxContext extends Objectx {

    private static ComxContext context;

    public static ComxContext getContext() {
        return context;
    }

    protected static void setContext(ComxContext context) {
        ComxContext.context = context;
    }

    /**
     * 获取缓存
     *
     * @return
     */
    public abstract String get(String name);

    /**
     * 获取Core-Application-Dao
     *
     * @return
     */
    public abstract ComxPagingDAO getCadao();

    /**
     * 获取其他dao
     *
     * @param daoName
     * @return
     */
    public abstract ComxPagingDAO getDao(String daoName);

    /**
     * 指定文件夹加载spring配置文件
     */
    public abstract void loadBeans(String tempFilePath) throws IOException;

    /**
     * 指定文件加载spring配置文件
     */
    public abstract void loadBean(String configLocation) throws IOException;

    /**
     * @return redis 管理
     */
    public abstract CacheManager getCacheManager();

    /**
     * @return 获取缓存刷新对象
     */
    public abstract IRefresher getRefresher();

    /**
     * @return 获取yml执行驱动对象
     */
    public abstract YmlChainDriver getYmlChainDriver();

    /**
     * @return 获取yml以及class的相关动态加载对象
     */
    public abstract YmlChainDriverReloader getYmlChainDriverReloader();
}
