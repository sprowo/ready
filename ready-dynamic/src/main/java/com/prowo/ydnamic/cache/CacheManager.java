package com.prowo.ydnamic.cache;

import java.util.Map;

public abstract class CacheManager implements ICacheQuery, ICacheUpdate {
    /**
     * 内嵌缓存
     */
    protected CacheManager defaultCacheManager;

    public CacheManager getDefaultCacheManager() {
        return this.defaultCacheManager;
    }

    public void setDefaultCacheManager(CacheManager defaultCacheManager) {
        this.defaultCacheManager = defaultCacheManager;
    }

    /**
     * 缓存刷新
     */
    public void refresh() {

    }

    /**
     * 移除对应key的缓存
     *
     * @param key
     */
    protected abstract void expire(String key, long timeout);

    public void setMap(String key, Map<String, String> value, long timeout) {
        setMap(key, value);
        expire(key, timeout);
    }

    public void setString(String key, String value, long timeout) {
        setString(key, value);
        expire(key, timeout);
    }

}
