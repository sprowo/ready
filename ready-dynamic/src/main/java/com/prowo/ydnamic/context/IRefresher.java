package com.prowo.ydnamic.context;

/**
 * 加载表数据到缓存
 */
public interface IRefresher {
    void loadUsers() throws Exception;

    void loadDicts() throws Exception;

    void loadConfigs() throws Exception;

    void loadIpAdresses() throws Exception;

    void loadExceptions() throws Exception;

    void loadMessages() throws Exception;

    void loadExt() throws Exception;

    void refreshCache() throws Exception;

}
