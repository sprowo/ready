package com.prowo.ydnamic.cache;

import java.util.Map;
import java.util.Set;

public interface ICacheQuery {

    /**
     * 得到缓存对象
     *
     * @param key
     * @return
     */
    @Deprecated
    <T> T getCacheObject(String key);

    String getString(String key);

    Integer getInt(String key);

    Long getLong(String key);

    Map<String, String> getMap(String key);

    Set<String> keys(String pattern);

}
