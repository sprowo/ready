package com.prowo.ydnamic.cache;

import java.util.Map;

public interface ICacheUpdate {

    void setString(String key, String value);


    void setMap(String key, Map<String, String> value);

    /**
     * 移除对应key的缓存
     *
     * @param key
     */
    void removeCache(String key);

    /**
     * 清除缓存
     *
     * @param pattern
     */
    void clearCache(String pattern);
}
